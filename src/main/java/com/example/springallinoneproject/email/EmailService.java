package com.example.springallinoneproject.email;

import com.example.springallinoneproject.api_payload.status_code.ErrorStatus;
import com.example.springallinoneproject.auth.VerificationCode;
import com.example.springallinoneproject.auth.VerificationCodeRepository;
import com.example.springallinoneproject.exception.GeneralException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String serviceEmail;
    @Value("${spring.mail.templates.logo-path}")
    private Resource logoFile;

    private final Integer EXPIRATION_TIME_IN_MINUTES = 5;
    private final String VERIFICATION_CODE_MAIL_SUBJECT = "Email Verification For %s";

    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;
    private final SpringTemplateEngine templateEngine;

    public void sendSimpleVerificationMail(String to, LocalDateTime sentAt) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(serviceEmail);
        mailMessage.setTo(to);
        mailMessage.setSubject(String.format(VERIFICATION_CODE_MAIL_SUBJECT, to));

        VerificationCode verificationCode = generateVerificationCode(sentAt);
        verificationCodeRepository.save(verificationCode);

        String text = verificationCode.generateCodeMessage();
        mailMessage.setText(text);

        mailSender.send(mailMessage);
    }

    public void sendVerificationMailWithTemplate(String to, LocalDateTime sentAt) throws MessagingException {
        VerificationCode verificationCode = generateVerificationCode(sentAt);
        verificationCodeRepository.save(verificationCode);

        HashMap<String, Object> templateModel = new HashMap<>();
        templateModel.put("verificationCode", verificationCode.generateCodeMessage());

        String subject = String.format(VERIFICATION_CODE_MAIL_SUBJECT, to);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = templateEngine.process("verification-code.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
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

    private void sendHtmlMessage(String to, String subject, String htmlBody)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(serviceEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("logo.png", logoFile);

        mailSender.send(message);
    }
}
