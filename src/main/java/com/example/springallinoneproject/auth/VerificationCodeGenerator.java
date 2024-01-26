package com.example.springallinoneproject.auth;

import java.time.LocalDateTime;
import java.util.UUID;

public class VerificationCodeGenerator {
    private static final Integer EXPIRATION_TIME_IN_MINUTES = 5;

    public static VerificationCode generateVerificationCode(LocalDateTime sentAt) {
        String code = UUID.randomUUID().toString();
        return VerificationCode.builder()
                .code(code)
                .createAt(sentAt)
                .expirationTimeInMinutes(EXPIRATION_TIME_IN_MINUTES)
                .build();
    }
}
