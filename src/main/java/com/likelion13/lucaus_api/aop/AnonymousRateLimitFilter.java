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

    @Value("${ratelimit.limit:50}")
    private int limit;

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

        if ("/actuator/health".equals(uri) &&
                userAgent != null &&
                userAgent.contains("ELB-HealthChecker")) {
            filterChain.doFilter(request, response);
            return;
        }

        String fpid = getOrCreateFpid(request, response);
        String rateKey = "ratelimit:" + fpid;
        String blockKey = "ratelimit:block:" + fpid;
        String alertKey = "slackalerted:" + fpid;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(blockKey))) {
            response.setStatus(429);
            response.getWriter().write("Too many requests (FPID - Blocked)");
            return;
        }

        Long count = redisTemplate.opsForValue().increment(rateKey);
        if (count == 1) {
            redisTemplate.expire(rateKey, Duration.ofSeconds(10));
        }

        if (count > limit) {
            // 2시간 차단
            redisTemplate.opsForValue().set(blockKey, "1", Duration.ofHours(2));

            Boolean alreadyAlerted = redisTemplate.hasKey(alertKey);
            if (Boolean.FALSE.equals(alreadyAlerted)) {
                try {
                    sendSlackAlert(request, fpid, count);
                    redisTemplate.opsForValue().set(alertKey, "1", Duration.ofHours(2));
                } catch (Exception e) {
                    System.err.println("Slack 전송 실패: " + e.getMessage());
                }
            }

            response.setStatus(429);
            response.getWriter().write("Too many requests (FPID - Blocked)");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getOrCreateFpid(HttpServletRequest request, HttpServletResponse response) {
        // 기존 쿠키 확인
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("FPID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String uuid = UUID.randomUUID().toString();

        // Set-Cookie 수동 설정 (SameSite=None 포함)
        String cookieValue = String.format(
                "FPID=%s; Path=/; Max-Age=604800; HttpOnly; Secure; SameSite=None; Domain=.lucaus.info",
                uuid
        );
        response.setHeader("Set-Cookie", cookieValue);

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