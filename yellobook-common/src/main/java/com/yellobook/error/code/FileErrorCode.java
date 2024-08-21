package com.yellobook.error.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FileErrorCode implements ErrorCode{
    FILE_NOT_EXIST(HttpStatus.NOT_FOUND, "FILE-001", "파일이 존재하지 않습니다."),
    FILE_NOT_EXCEL(HttpStatus.BAD_REQUEST, "FILE-002", "올바른 엑셀 확장자가 아닙니다. .xlsx의 엑셀 파일이어야 합니다."),
    CELL_IS_EMPTY(HttpStatus.BAD_REQUEST, "FILE-003", "빈 값을 가진 셀이 존재합니다."),
    CELL_INVALID_TYPE(HttpStatus.BAD_REQUEST, "FILE-004", "셀의 타입이 올바르지 않습니다."),
    ROW_HAS_EMPTY_CELL(HttpStatus.BAD_REQUEST, "FILE-005", "row에 빈 cell이 존재합니다."),
    FILE_IO_FAIL(HttpStatus.BAD_REQUEST, "FILE-006", "파일 IO에 실패했습니다."),
    SKU_DUPLICATE(HttpStatus.CONFLICT, "FILE-007", "중복된 SKU가 존재합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
