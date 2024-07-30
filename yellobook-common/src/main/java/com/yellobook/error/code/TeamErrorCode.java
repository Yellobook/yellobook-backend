package com.yellobook.error.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TeamErrorCode implements ErrorCode {
    USER_NOT_JOINED_ANY_TEAM(HttpStatus.FORBIDDEN, "TEAM-001", "해당 사용자는 아직 팀에 참여하지 않았습니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM-002", "팀을 찾을 수 없습니다."),
    USER_NOT_IN_THAT_TEAM(HttpStatus.FORBIDDEN, "TEAM-003", "해당 팀에 속하지 않은 사용자입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}