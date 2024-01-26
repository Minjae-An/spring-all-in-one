package com.example.springallinoneproject.email;

import com.example.springallinoneproject.api_payload.CommonResponse;
import com.example.springallinoneproject.api_payload.status_code.SuccessStatus;
import com.example.springallinoneproject.email.dto.EmailRequest.EmailForVerificationRequest;
import com.example.springallinoneproject.email.dto.EmailRequest.VerificationCodeRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/verify-email")
    public CommonResponse<Void>
    getEmailForVerification(@RequestBody EmailForVerificationRequest request) {
        LocalDateTime requestedAt = LocalDateTime.now();
        emailService.sendSimpleVerificationMail(request.getEmail(), requestedAt);
        return CommonResponse.of(SuccessStatus._ACCEPTED, null);
    }

    @PostMapping("/verification-code")
    public CommonResponse<String>
    verificationByCode(@RequestBody VerificationCodeRequest request) {
        LocalDateTime requestedAt = LocalDateTime.now();
        emailService.verifyCode(request.getCode(), requestedAt);
        return CommonResponse.ok("정상 인증 완료");
    }
}
