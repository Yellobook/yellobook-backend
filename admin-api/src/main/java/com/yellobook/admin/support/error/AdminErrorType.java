package com.yellobook.admin.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum AdminErrorType {
    INTERNAL_SERVER_ERROR(AdminErrorCode.SYS01, HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 내부 오류가 발생했습니다. 다시 시도해주세요.", LogLevel.ERROR),
    SERVICE_UNAVAILABLE(AdminErrorCode.SYS02, HttpStatus.SERVICE_UNAVAILABLE,
            "현재 서비스 이용이 불가능합니다. 나중에 다시 시도해주세요.", LogLevel.INFO),

    INVALID_REQUEST(AdminErrorCode.VAL01, HttpStatus.BAD_REQUEST,
            "요청 데이터가 유효하지 않습니다.", LogLevel.WARN),
    MISSING_PARAMETER(AdminErrorCode.VAL02, HttpStatus.BAD_REQUEST,
            "필수 요청 파라미터가 누락되었습니다.", LogLevel.WARN),
    INVALID_PARAMETER_TYPE(AdminErrorCode.VAL03, HttpStatus.BAD_REQUEST,
            "요청 파라미터의 타입이 올바르지 않습니다.", LogLevel.WARN),
    TERMS_NOT_FOUND(AdminErrorCode.TERMS01, HttpStatus.NOT_FOUND, "존재하지 않는 약관입니다.", LogLevel.WARN);

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
