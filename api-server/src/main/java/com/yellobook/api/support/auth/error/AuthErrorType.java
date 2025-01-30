package com.yellobook.api.support.auth.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum AuthErrorType {
    AUTHORIZE_FAILED(AuthErrorCode.AUTH001, HttpStatus.UNAUTHORIZED,
            "사용자 인가에 실패했습니다.", LogLevel.WARN),
    ACCESS_DENIED(AuthErrorCode.AUTH002, HttpStatus.FORBIDDEN,
            "접근이 거부되었습니다. 이 리소스에 대한 권한이 없습니다.", LogLevel.WARN),
    REFRESH_TOKEN_COOKIE_MISMATCH(AuthErrorCode.AUTH003, HttpStatus.FORBIDDEN, "사용자의 리프레시토큰과 쿠키의 리프레시토큰이 일치하지 않습니다.",
            LogLevel.WARN),
    REFRESH_TOKEN_NOT_FOUND(AuthErrorCode.AUTH004, HttpStatus.UNAUTHORIZED, "리프레시토큰이 존재하지 않습니다.", LogLevel.WARN),
    ACCESS_TOKEN_BLACKLISTED(AuthErrorCode.AUTH005, HttpStatus.UNAUTHORIZED, "해당 액세스토큰은 블랙리스트에 등록되어 사용할 수 없습니다.",
            LogLevel.WARN),
    ACCESS_TOKEN_NOT_FOUND_IN_HEADER(AuthErrorCode.AUTH006, HttpStatus.UNAUTHORIZED, "요청 헤더에 엑세스토큰이 존재하지 않습니다.",
            LogLevel.INFO),
    OAUTH2_PROVIDER_INVALID(AuthErrorCode.AUTH006, HttpStatus.UNAUTHORIZED, "소셜로그인 제공자가 올바르지 않습니다.",
            LogLevel.ERROR),
    REFRESH_TOKEN_COOKIE_NOT_FOUND(AuthErrorCode.AUTH007, HttpStatus.FORBIDDEN, "쿠키에 리프레시토큰이 존재하지 않습니다.",
            LogLevel.WARN),
    UNREADABLE_TOKEN(AuthErrorCode.AUTH008, HttpStatus.FORBIDDEN, "토큰 읽기에 실패했습니다.",
            LogLevel.ERROR);

    private final AuthErrorCode code;
    private final HttpStatus status;
    private final String message;
    private final LogLevel logLevel;

    AuthErrorType(AuthErrorCode code, HttpStatus status, String message, LogLevel logLevel) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.logLevel = logLevel;
    }

    public String getCode() {
        return code.getErrorCode();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
