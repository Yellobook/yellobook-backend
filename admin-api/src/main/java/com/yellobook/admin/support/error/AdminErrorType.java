package com.yellobook.admin.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum AdminErrorType {
    AUTH_FAILED(AdminErrorCode.ADMIN01, HttpStatus.UNAUTHORIZED, "관리자 인증 정보가 없습니다.", LogLevel.ERROR),
    TERMS_NOT_FOUND(AdminErrorCode.ADMIN02, HttpStatus.NOT_FOUND, "존재하지 않는 약관입니다.", LogLevel.WARN),
    ACCOUNT_NOT_FOUND(AdminErrorCode.ADMIN03, HttpStatus.NOT_FOUND, "인증 정보에 해당하는 관리자계정이 존재하지 않습니다.",
            LogLevel.ERROR),
    ACCESS_TOKEN_NOT_FOUND_IN_HEADER(AdminErrorCode.ADMIN04, HttpStatus.UNAUTHORIZED, "엑세스토큰이 헤더에 존재하지 않습니다.",
            LogLevel.WARN),
    FORBIDDEN(AdminErrorCode.ADMIN05, HttpStatus.FORBIDDEN, "관리자만 접근할 수 있는 리소스입니다.", LogLevel.WARN),

    AUTH_ERROR(AdminErrorCode.ADMIN06, HttpStatus.INTERNAL_SERVER_ERROR, "인증 오류 발생", LogLevel.ERROR);
    private final AdminErrorCode code;
    private final HttpStatus status;
    private final String message;
    private final LogLevel logLevel;

    AdminErrorType(AdminErrorCode code, HttpStatus status, String message, LogLevel logLevel) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.logLevel = logLevel;
    }

    public String getCode() {
        return code.getCode();
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
