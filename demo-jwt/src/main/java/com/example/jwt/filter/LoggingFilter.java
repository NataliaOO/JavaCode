package com.example.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long start = System.currentTimeMillis();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");
        String jwtInfo = (authHeader != null && authHeader.startsWith("Bearer "))
                ? "JWT present"
                : "no JWT";

        logger.debug("Incoming request: {} {} from {} ({})",
                method, uri, request.getRemoteAddr(), jwtInfo);

        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - start;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";

        logger.info("Request: {} {} user={} status={} took={}ms",
                method, uri, username, response.getStatus(), duration);
    }
}
