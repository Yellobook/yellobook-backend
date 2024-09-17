package com.yellobook.error.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum InformErrorCode implements ErrorCode {
    INFORM_NOT_FOUND(HttpStatus.NOT_FOUND, "INFORM-001", "공지(업무)가 존재하지 않습니다."),
    INFORM_MEMBER_NOT_MATCH(HttpStatus.FORBIDDEN, "INFORM-002", "공지(업무)의 작성자가 아닙니다."),
    NOT_MENTIONED(HttpStatus.NOT_FOUND, "INFORM-003", "공지(업무)에 언급되지 않은 멤버입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
