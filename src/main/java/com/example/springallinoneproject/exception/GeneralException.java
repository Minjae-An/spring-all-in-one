package com.example.springallinoneproject.exception;

import com.example.springallinoneproject.api_payload.status_code.ErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class GeneralException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public String getErrorCode() {
        return errorStatus.getCode();
    }

    public String getErrorReason() {
        return errorStatus.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return errorStatus.getHttpStatus();
    }
}
