package com.example.springallinoneproject.sms;

import com.example.springallinoneproject.api_payload.CommonResponse;
import com.example.springallinoneproject.api_payload.status_code.SuccessStatus;
import com.example.springallinoneproject.sms.dto.SmsRequest.PhoneNumberForVerificationRequest;
import com.example.springallinoneproject.sms.dto.SmsRequest.VerificationCodeRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/verify-phone-number")
    public CommonResponse<Void>
    getPhoneNumberForVerification(@RequestBody PhoneNumberForVerificationRequest request) {
        LocalDateTime sentAt = LocalDateTime.now();
        smsService.sendVerificationMessage(request.getPhoneNumber(), sentAt);
        return CommonResponse.of(SuccessStatus._ACCEPTED, null);
    }

    @PostMapping("/phone-number/verification-code")
    public CommonResponse<String> verificationByCode(@RequestBody VerificationCodeRequest request) {
        LocalDateTime verifiedAt = LocalDateTime.now();
        smsService.verifyCode(request.getCode(), verifiedAt);
        return CommonResponse.ok("정상 인증 되었습니다.");
    }
}
