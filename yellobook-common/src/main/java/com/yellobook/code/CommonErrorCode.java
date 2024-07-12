package com.yellobook.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonErrorCode implements ErrorCode {

    // 서버 및 시스템 관련 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS-001", "서버 내부 오류가 발생했습니다. 다시 시도해주세요."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SYS-002", "현재 서비스 이용이 불가능합니다. 나중에 다시 시도해주세요."),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "SYS-003", "요청 시간이 초과되었습니다. 다시 시도해주세요."),

    // 클라이언트 관련 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "CLIENT-001", "잘못된 요청입니다. 요청 내용을 다시 확인해주세요."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "CLIENT-002", "인증이 필요합니다. 인증 정보를 제공해주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "CLIENT-003", "접근이 거부되었습니다. 권한을 확인해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "CLIENT-004", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "CLIENT-005", "허용되지 않은 메서드입니다. 요청 방식을 확인해주세요."),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "CLIENT-006", "요청한 리소스가 허용되지 않는 형식입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "CLIENT-007", "지원되지 않는 미디어 형식입니다."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "CLIENT-008", "요청이 너무 많습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}