package com.likelion13.lucaus_api.aop;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Component
public class AnonymousRateLimitFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;

    @Value("${ratelimit.limit:100}")
    private int limit;

    @Value("${ratelimit.windowSeconds:7200}")
    private int windowSeconds;

    @Value("${slack.webhook.url}")
    private String slackWebhookUrl;

    public AnonymousRateLimitFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");

        // ALB 헬스체크 우회
        if ("/actuator/health".equals(uri) &&
                userAgent != null &&
                userAgent.contains("ELB-HealthChecker")) {
            filterChain.doFilter(request, response);
            return;
        }

        String fpid = getOrCreateFpid(request, response);
        String rateKey = "ratelimit:" + fpid;
        String alertKey = "slackalerted:" + fpid;

        Long count = redisTemplate.opsForValue().increment(rateKey);
        if (count == 1) {
            redisTemplate.expire(rateKey, Duration.ofSeconds(windowSeconds));
        }

        if (count > limit) {
            redisTemplate.expire(rateKey, Duration.ofSeconds(windowSeconds));
            Boolean alreadyAlerted = redisTemplate.hasKey(alertKey);
            if (Boolean.FALSE.equals(alreadyAlerted)) {
                try {
                    sendSlackAlert(request, fpid, count);
                    redisTemplate.opsForValue().set(alertKey, "1", Duration.ofSeconds(windowSeconds));
                } catch (Exception e) {
                    System.err.println("Slack 전송 실패: " + e.getMessage());
                }
            }

            response.setStatus(429);
            response.getWriter().write("Too many requests (FPID)");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getOrCreateFpid(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("FPID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String uuid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("FPID", uuid);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(cookie);
        return uuid;
    }

    private void sendSlackAlert(HttpServletRequest request, String fpid, Long count) {
        try {
            String message = String.format(
                    "* 📱과도한 개인 요청 차단 알림*\n" +
                            "- IP: `%s`\n" +
                            "- FPID: `%s`\n" +
                            "- URI: `%s`\n" +
                            "- 요청 수: `%d`\n",
                    request.getRemoteAddr(),
                    fpid,
                    request.getRequestURI(),
                    count
            );

            var json = "{\"text\":\"" + message.replace("\"", "\\\"") + "\"}";
            var url = new java.net.URL(slackWebhookUrl);
            var conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.getOutputStream().write(json.getBytes("utf-8"));
            conn.getInputStream().close();

        } catch (Exception e) {
            System.err.println("Slack 전송 실패: " + e.getMessage());
        }
    }
}