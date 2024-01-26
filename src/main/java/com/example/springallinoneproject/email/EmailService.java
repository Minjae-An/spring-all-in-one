package com.example.springallinoneproject.email;

import com.example.springallinoneproject.api_payload.status_code.ErrorStatus;
import com.example.springallinoneproject.auth.VerificationCode;
import com.example.springallinoneproject.auth.VerificationCodeRepository;
import com.example.springallinoneproject.exception.GeneralException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username")
    private String serviceEmail;
    private final Integer EXPIRATION_TIME_IN_MINUTES = 5;

    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;

    public void sendSimpleVerificationMail(String to, LocalDateTime sentAt) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(serviceEmail);
        mailMessage.setTo(to);
        mailMessage.setSubject(String.format("Email Verification For %s", to));

        VerificationCode verificationCode = generateVerificationCode(sentAt);
        verificationCodeRepository.save(verificationCode);

        String text = verificationCode.generateCodeMessage();
        mailMessage.setText(text);

        mailSender.send(mailMessage);
    }

    public void verifyCode(String code, LocalDateTime verifiedAt) {
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
                .orElseThrow(() -> new GeneralException(ErrorStatus._VERIFICATION_CODE_NOT_FOUND));

        if (verificationCode.isExpired(verifiedAt)) {
            throw new GeneralException(ErrorStatus._VERIFICATION_CODE_EXPIRED);
        }

        verificationCodeRepository.remove(verificationCode);
    }

    private VerificationCode generateVerificationCode(LocalDateTime sentAt) {
        String code = UUID.randomUUID().toString();
        return VerificationCode.builder()
                .code(code)
                .createAt(sentAt)
                .expirationTimeInMinutes(EXPIRATION_TIME_IN_MINUTES)
                .build();
    }
}
