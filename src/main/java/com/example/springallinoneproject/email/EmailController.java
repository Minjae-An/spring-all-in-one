package com.example.springallinoneproject.email;

import com.example.springallinoneproject.api_payload.CommonResponse;
import com.example.springallinoneproject.api_payload.status_code.SuccessStatus;
import com.example.springallinoneproject.email.dto.EmailRequest.EmailForVerificationRequest;
import com.example.springallinoneproject.email.dto.EmailRequest.VerificationCodeRequest;
import jakarta.mail.MessagingException;
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
        LocalDateTime sentAt = LocalDateTime.now();
        emailService.sendSimpleVerificationMail(request.getEmail(), sentAt);
        return CommonResponse.of(SuccessStatus._ACCEPTED, null);
    }

    @PostMapping("/v2/verify-email")
    public CommonResponse<Void>
    getEmailForVerificationV2(@RequestBody EmailForVerificationRequest request)
            throws MessagingException {
        LocalDateTime sentAt = LocalDateTime.now();
        emailService.sendVerificationMailWithTemplate(request.getEmail(), sentAt);
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
