package com.cloudnrg.api.shared.infrastructure.filters;

import com.cloudnrg.api.shared.infrastructure.ratelimiting.bucket4j.configuration.RateLimitConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.github.bucket4j.Bucket;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimitConfig rateLimitConfig;

    // Adjust these values as needed
    private static final int REQUESTS_PER_MINUTE = 100;
    private static final String RATE_LIMIT_HEADER = "X-Rate-Limit-Retry-After";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extract token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing token");
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        // Get user's bucket
        Bucket bucket = rateLimitConfig.resolveBucket(token, REQUESTS_PER_MINUTE);

        // Check rate limit
        if (bucket.tryConsume(1)) {
            // Add remaining tokens to headers
            response.addHeader("X-Rate-Limit-Remaining",
                    String.valueOf(bucket.getAvailableTokens()));
            filterChain.doFilter(request, response);
        } else {
            // Calculate wait time
            long waitSeconds = Math.round(
                    bucket.estimateAbilityToConsume(1).getNanosToWaitForRefill() / 1e9
            );

            // Write response directly
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader(RATE_LIMIT_HEADER, String.valueOf(waitSeconds));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            response.getWriter().write(
                    "{\"error\":\"rate_limit_exceeded\"," +
                            "\"message\":\"Too many requests. Try again in " + waitSeconds + " seconds\"}"
            );
            response.flushBuffer();

            // STOP FILTER CHAIN EXECUTION
            return;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Exclude specific paths from rate limiting
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/authentication")
                || path.contains("swagger")
                || path.contains("api-docs");
    }

}
