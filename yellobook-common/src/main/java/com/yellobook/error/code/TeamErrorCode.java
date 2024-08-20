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
    USER_NOT_IN_THAT_TEAM(HttpStatus.FORBIDDEN, "TEAM-003", "해당 팀에 속하지 않은 사용자입니다."),
    TEAM_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "TEAM-004", "팀 생성에 실패하였습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM-005", "해당 유저를 찾을 수 없습니다."),
    VIEWER_CANNOT_INVITE(HttpStatus.FORBIDDEN, "TEAM-006", "뷰어는 초대권한이 없습니다."),
    ADMIN_EXISTS(HttpStatus.CONFLICT, "TEAM-007", "관리자가 이미 존재합니다."),
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM-008", "초대장을 찾을 수 없습니다."),
    INVALID_INVITATION(HttpStatus.NOT_FOUND, "TEAM-009", "유효하지 않은 초대장 입니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.CONFLICT, "TEAM-010", "이미 존재하는 멤버입니다."),
    EXIST_TEAM_NAME(HttpStatus.CONFLICT, "TEAM-011", "이미 존재하는 팀 이름입니다."),
    MENTIONED_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM-012", "존재하지 않는 사용자에 대한 언급입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}