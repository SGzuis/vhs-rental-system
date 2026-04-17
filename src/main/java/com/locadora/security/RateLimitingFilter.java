package com.locadora.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private final Map<String, Long> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 100;
    private static final long TIME_WINDOW = 60000; // 1 minuto
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                   FilterChain chain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();
        
        Long lastRequest = requestCounts.get(clientIp);
        if (lastRequest != null && (currentTime - lastRequest) < (TIME_WINDOW / MAX_REQUESTS)) {
            // Lógica simplificada de rate limit
            // Para produção real, usar Redis ou biblioteca como Bucket4j
        }
        
        requestCounts.put(clientIp, currentTime);
        chain.doFilter(request, response);
    }
}
