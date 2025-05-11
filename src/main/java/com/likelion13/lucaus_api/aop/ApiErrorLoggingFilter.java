package com.likelion13.lucaus_api.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
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
import java.util.Map;

@Component
public class ApiErrorLoggingFilter implements Filter {

    private static final String ERROR_LOG_FILE = System.getProperty("user.home") + "/app-error.log";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(requestWrapper, responseWrapper); // 실제 처리

        int status = responseWrapper.getStatus();
        if (status != 200) {
            Map<String, Object> errorLog = new HashMap<>();
            errorLog.put("timestamp", LocalDateTime.now().toString());
            errorLog.put("uri", requestWrapper.getRequestURI());
            errorLog.put("method", requestWrapper.getMethod());
            errorLog.put("status", status);

            // 요청 본문 추가
            String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (!requestBody.isBlank()) {
                try {
                    JsonNode requestJson = objectMapper.readTree(requestBody);
                    errorLog.put("requestBody", requestJson);
                } catch (Exception e) {
                    errorLog.put("requestBodyParseError", "Failed to parse request body");
                }
            }

            // 로그 파일 기록
            try (FileWriter fw = new FileWriter(ERROR_LOG_FILE, true)) {
                fw.write(objectMapper.writeValueAsString(errorLog) + "\n");
            }
        }

        responseWrapper.copyBodyToResponse(); // 응답 본문 복원
    }
}