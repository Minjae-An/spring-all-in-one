package com.example.springallinoneproject.user.login.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springallinoneproject.user.dto.LoggedInUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String secret;
    private int expirationTimeSeconds = 60;
    @Value("${jwt.refresh-token-expiration-seconds}")
    private int refreshTokenExpirationSeconds;
    private String tokenPrefix = "Bearer ";
    private ObjectMapper objectMapper = new ObjectMapper();

    public boolean isIncludeTokenPrefix(String authorization) {
        return authorization.split(" ")[0].equals(tokenPrefix.trim());
    }

    public String extractTokenFromAuthorization(String authorization) {
        return authorization.replace(tokenPrefix, "");
    }


    public String createAuthorization(LoggedInUser loggedInUser, Instant current) {
        String token = createAccessToken(loggedInUser, current);
        return tokenPrefix.concat(token);
    }

    public boolean isTokenExpired(String token) {
        Instant expiredAt = JWT.decode(token)
                .getExpiresAtAsInstant();

        return expiredAt.isBefore(Instant.now());
    }

    public boolean isTokenNotManipulated(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret))
                    .build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public LoggedInUser extractUserFromToken(String token) {
        String payload = JWT.decode(token)
                .getPayload();

        byte[] decodedBytes = Base64.getDecoder().decode(payload);
        String decodedPayload = new String(decodedBytes);

        return parseUserFromJwt(decodedPayload);
    }


    public String createAccessToken(LoggedInUser loggedInUser, Instant current) {
        return JWT.create()
                .withSubject(loggedInUser.getEmail())
                .withExpiresAt(current.plusSeconds(expirationTimeSeconds))
                .withClaim("email", loggedInUser.getEmail())
                .withClaim("username", loggedInUser.getUsername())
                .sign(Algorithm.HMAC512(secret));
    }

    public String createRefreshToken(Long id, Instant current) {
        return JWT.create()
                .withExpiresAt(current.plusSeconds(refreshTokenExpirationSeconds))
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(secret));
    }

    private LoggedInUser parseUserFromJwt(String decodedPayload) {
        try {
            LinkedHashMap<String, Object> payloadMap = objectMapper.readValue(decodedPayload, LinkedHashMap.class);
            String email = (String) payloadMap.get("email");
            String username = (String) payloadMap.get("username");
            return LoggedInUser.builder()
                    .email(email)
                    .username(username)
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
