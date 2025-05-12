package com.likelion13.lucaus_api.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ApiLoggingFilter implements Filter {

    private static final String LOG_FILE =
            Optional.ofNullable(System.getProperty("log.file.path"))
                    .orElseGet(() -> {
                        String os = System.getProperty("os.name").toLowerCase();
                        if (os.contains("linux")) {
                            return "/home/ubuntu/app-data.log";
                        } else {
                            return System.getProperty("user.home") + "/app-data.log";
                        }
                    });
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<String> logTargets = List.of(
            "/api/notices",
            "/api/food-truck/review",
            "/api/food-truck/",
            "/api/booth/",
            "/api/booth/review",
            "/api/auth/signup",
            "/api/stamp",
            "/api/stamp/stamp-booth",
            "/api/stamp/reward"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper reqWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper resWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(reqWrapper, resWrapper); // request/response 처리

        if (resWrapper.getStatus() == 200) {
            String uri = reqWrapper.getRequestURI();

            boolean shouldLog = logTargets.stream().anyMatch(uri::contains);
            if (!shouldLog) {
                resWrapper.copyBodyToResponse(); // 응답 복원
                return;
            }

            Map<String, Object> log = new HashMap<>();
            log.put("timestamp", LocalDateTime.now().toString());
            log.put("method", reqWrapper.getMethod());
            log.put("uri", uri);

                // 요청 분석
                String reqBody = new String(reqWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                if (!reqBody.isBlank()) {
                    try {
                        JsonNode json = objectMapper.readTree(reqBody);
                        if (reqWrapper.getRequestURI().contains("/stamp/stamp-booth")) {
                            log.put("stampBoothId", json.path("stampBoothId").asText());
                            log.put("type", json.path("type").asText());
                        } else if (reqWrapper.getRequestURI().contains("/stamp/reward")) {
                            log.put("type", json.path("type").asText());
                            log.put("degree", json.path("degree").asText());
                        }
                    } catch (Exception ignored) {}
                }

                // 응답 분석
                String resBody = new String(resWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                try {
                    JsonNode json = objectMapper.readTree(resBody);
                    if (json.path("isSuccess").asBoolean(false)) {
                        JsonNode result = json.path("result");
                        if (result.isArray() && result.size() > 0) {
                            if (reqWrapper.getRequestURI().contains("/food-truck/")) {
                                log.put("foodTruckId", result.get(0).path("foodTruckId").asText());
                            } else if (reqWrapper.getRequestURI().contains("/booth/")) {
                                log.put("boothId", result.get(0).path("boothId").asText());
                            }
                        }
                    }
                } catch (Exception ignored) {}

                try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
                    fw.write(objectMapper.writeValueAsString(log) + "\n");
                }
            }
        resWrapper.copyBodyToResponse(); // 응답 복원
    }
}