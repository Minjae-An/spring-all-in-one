package com.example.springallinoneproject.api_payload.status_code;

import org.springframework.http.HttpStatus;

public enum ErrorStatus implements BaseCode{
    // common
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // jwt
    _NO_BEARER_PREFIX(HttpStatus.BAD_REQUEST, "JWT4001", "Bearer prefix가 authorzation 헤더 값에 존재하지 않습니다."),
    _TOKEN_SIGNATURE_NOT_VALID(HttpStatus.BAD_REQUEST, "JWT4002", "시그니처가 올바르지 않은 JWT입니다."),
    _ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "JWT4003", "access token이 만료되었습니다. refresh token을 보내주세요."),

    // user
    _USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "해당 사용자가 존재하지 않습니다."),
    _USER_NOT_FOUND_BY_EMAIL(HttpStatus.BAD_REQUEST, "USER4002", "해당 이메일의 사용자가 존재하지 않습니다."),
    _ALREADY_JOINED_USER(HttpStatus.BAD_REQUEST, "USER4003", "해당 이메일로 이미 가입한 유저가 존재합니다."),

    // user image
    _USER_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_IMAGE4001", "해당 사용자 이미지가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
