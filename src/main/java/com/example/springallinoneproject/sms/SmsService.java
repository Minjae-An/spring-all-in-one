package com.example.springallinoneproject.sms;

import com.example.springallinoneproject.api_payload.status_code.ErrorStatus;
import com.example.springallinoneproject.auth.VerificationCode;
import com.example.springallinoneproject.auth.VerificationCodeGenerator;
import com.example.springallinoneproject.auth.VerificationCodeRepository;
import com.example.springallinoneproject.exception.GeneralException;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${spring.sms.api-key}")
    private String apiKey;
    @Value("${spring.sms.api-secret}")
    private String apiSecret;
    @Value("${spring.sms.provider}")
    private String smsProvider;
    @Value("${spring.sms.sender}")
    private String smsSender;

    private DefaultMessageService messageService;

    private final VerificationCodeRepository verificationCodeRepository;

    @PostConstruct
    public void init(){
        messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecret,
                smsProvider
        );
    }

    public void sendVerificationMessage(String to, LocalDateTime sentAt){
        Message message = new Message();
        message.setFrom(smsSender);
        message.setTo(to);

        VerificationCode verificationCode = VerificationCodeGenerator
                .generateVerificationCode(sentAt);
        verificationCodeRepository.save(verificationCode);

        String text = verificationCode.generateCodeMessage();
        message.setText(text);

        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public void verifyCode(String code, LocalDateTime verifiedAt){
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
                .orElseThrow(() -> new GeneralException(ErrorStatus._VERIFICATION_CODE_NOT_FOUND));

        if(verificationCode.isExpired(verifiedAt)){
            throw new GeneralException(ErrorStatus._VERIFICATION_CODE_EXPIRED);
        }

        verificationCodeRepository.remove(verificationCode);
    }
}
