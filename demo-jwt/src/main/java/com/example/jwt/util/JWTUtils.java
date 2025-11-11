package com.example.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);
    private SecretKey secretKey;
    private static final long EXPIRATION_TIME = 30 * 1000L; //86400000; // 24 часа
    private static final long REFRESH_EXPIRATION_TIME = EXPIRATION_TIME * 7;

    public JWTUtils() {
        // В идеале – вынести секрет в application.yml и зашифровать/использовать env
        String secretString = "very-strong-and-long-secret-key-for-jwt-demo-1234567890";
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        // добавляем роли в токен
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        logger.info("Generating access JWT for user {}", userDetails.getUsername());
        return createToken(claims, userDetails.getUsername(), EXPIRATION_TIME);
    }

    // Генерация refresh-токена (минимум данных, дольше живёт)
    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        logger.info("Generating refresh JWT for user {}", userDetails.getUsername());
        return createToken(claims, userDetails.getUsername(), REFRESH_EXPIRATION_TIME);
    }

    private String createToken(HashMap<String, Object> claims, String subject, long expirationMillis) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}