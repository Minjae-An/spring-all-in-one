package com.example.springallinoneproject.user.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springallinoneproject.user.dto.LoggedInUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String secret;
    private int expirationTimeMillis = 864_000_000; // 10일(밀리 초 단위)
    private String tokenPrefix = "Bearer ";
    private ObjectMapper objectMapper = new ObjectMapper();

    public boolean isIncludeTokenPrefix(String header) {
        return header.split(" ")[0].equals(tokenPrefix.trim());
    }


    public String extractTokenFromHeader(String header) {
        return header.replace(tokenPrefix, "");
    }


    public String createToken(LoggedInUser loggedInUser, Instant currentDate) {
        String token = JWT.create()
                .withSubject(loggedInUser.getEmail())
                .withExpiresAt(currentDate.plusMillis(expirationTimeMillis))
                .withClaim("email", loggedInUser.getEmail())
                .withClaim("username", loggedInUser.getUsername())
                .sign(Algorithm.HMAC512(secret));

        return tokenPrefix.concat(token);
    }

    public boolean isTokenExpired(String token) {
        Instant expiredAt = JWT.require(Algorithm.HMAC512(secret))
                .build().verify(token)
                .getExpiresAtAsInstant();

        return expiredAt.isBefore(Instant.now());
    }

    public boolean isTokenNotManipulated(String token) {
        return JWT.require(Algorithm.HMAC512(secret))
                .build().verify(token)
                .getSignature()
                .equals(secret);
    }

    public LoggedInUser extractUserFromToken(String token) {
        String payload = JWT.decode(token)
                .getPayload();

        byte[] decodedBytes = Base64.getDecoder().decode(payload);
        String decodedPayload = new String(decodedBytes);

        return parseUserFromJwt(decodedPayload);
    }

    private LoggedInUser parseUserFromJwt(String decodedPayload) {
        try {
            LinkedHashMap<String, Object> payloadMap = objectMapper.readValue(decodedPayload, LinkedHashMap.class);
            String email = (String) payloadMap.get("email");
            String username = (String) payloadMap.get("username");
            return new LoggedInUser(email, username);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
