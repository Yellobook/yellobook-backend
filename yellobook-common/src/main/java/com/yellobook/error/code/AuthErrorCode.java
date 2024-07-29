package com.yellobook.error.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements ErrorCode {

    // 인증 및 권한 관련 에러
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-001", "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-002", "접근이 거부되었습니다. 이 리소스에 대한 권한이 없습니다."),
    INSUFFICIENT_PERMISSIONS(HttpStatus.FORBIDDEN, "AUTH-003", "작업을 수행할 권한이 부족합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-004", "로그인에 실패했습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-005", "토큰의 유효기간이 만료되었습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "AUTH-006", "잘못된 토큰 형식입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-007", "쿠키에 refreshToken이 없습니다."),

    // 권한 애러
    ADMIN_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "AUTH-008", "관리자는 접근 권한이 없습니다."),
    ORDERER_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "AUTH-009", "주문자는 접근 권한이 없습니다."),
    VIEWER_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "AUTH-010", "뷰어는 접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}