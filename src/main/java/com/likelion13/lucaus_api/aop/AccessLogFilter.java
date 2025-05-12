package com.likelion13.lucaus_api.aop;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AccessLogFilter extends OncePerRequestFilter {

    private static final String LOG_FILE =
            System.getProperty("access.log.file", "/home/ubuntu/access.log");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String userAgent = request.getHeader("User-Agent");

        if ("/actuator/health".equals(uri) &&
                userAgent != null &&
                userAgent.contains("ELB-HealthChecker")) {
            filterChain.doFilter(request, response);
            return;
        }

        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long duration = System.currentTimeMillis() - start;

        String log = String.format("[%s] IP=%s METHOD=%s URI=%s STATUS=%d TIME=%dms UA=%s%n",
                LocalDateTime.now(), ip, method, uri, response.getStatus(), duration, userAgent);

        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(log);
        }
    }
}
