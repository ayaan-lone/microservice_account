package com.onlineBanking.account.config;

import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
        String token = extractToken(request);
        if (token == null) {
            sendUnauthorizedError(response);
            return false;
        }

        Long userId = validateAndGetUserId(token);
        if (userId == null) {
            sendUnauthorizedError(response);
            return false;
        }

        request.setAttribute("userId", userId);
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }

    private Long validateAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claims.get("userId", Long.class);
        } catch (JwtException e) {
            return null;
        }
    }

    private void sendUnauthorizedError(HttpServletResponse response) throws Exception {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request");
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}