package com.yellobook.error.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-001", "해당 사용자는 존재하지 않습니다."),
    MEMBER_TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-002", "사용자가 위치한 팀 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
