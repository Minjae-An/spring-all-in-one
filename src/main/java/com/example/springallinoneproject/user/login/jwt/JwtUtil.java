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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String secret;
    private int expirationTimeMillis = 864_000_000; // 10일(밀리 초 단위)
    private String tokenPrefix = "Bearer ";
    private ObjectMapper objectMapper = new ObjectMapper();

    public boolean isIncludeTokenPrefix(String authorization) {
        return authorization.split(" ")[0].equals(tokenPrefix.trim());
    }


    public String extractTokenFromAuthorization(String authorization) {
        return authorization.replace(tokenPrefix, "");
    }


    public String createAuthorization(LoggedInUser loggedInUser, Instant current) {
        String token = createToken(loggedInUser, current);
        return tokenPrefix.concat(token);
    }

    String createToken(LoggedInUser loggedInUser, Instant current) {
        String token = JWT.create()
                .withSubject(loggedInUser.getEmail())
                .withExpiresAt(current.plusMillis(expirationTimeMillis))
                .withClaim("email", loggedInUser.getEmail())
                .withClaim("username", loggedInUser.getUsername())
                .sign(Algorithm.HMAC512(secret));
        return token;
    }

    public boolean isTokenExpired(String token) {
        Instant expiredAt = JWT.decode(token)
                .getExpiresAtAsInstant();

        return expiredAt.isBefore(Instant.now());
    }

    public boolean isTokenNotManipulated(String token) {
        try{
            JWT.require(Algorithm.HMAC512(secret))
                    .build().verify(token)
                    .getSignature();

            return true;
        }catch(JWTVerificationException e){
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

    void setSecret(String secret){
        this.secret = secret;
    }

    int getExpirationTimeMillis() {
        return expirationTimeMillis;
    }
}
