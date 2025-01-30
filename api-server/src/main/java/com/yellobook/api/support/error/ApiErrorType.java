package com.yellobook.api.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ApiErrorType {
    INTERNAL_SERVER_ERROR(ApiErrorCode.SYS001, HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 내부 오류가 발생했습니다. 다시 시도해주세요.", LogLevel.ERROR),
    SERVICE_UNAVAILABLE(ApiErrorCode.SYS002, HttpStatus.SERVICE_UNAVAILABLE,
            "현재 서비스 이용이 불가능합니다. 나중에 다시 시도해주세요.", LogLevel.INFO),
    GATEWAY_TIMEOUT(ApiErrorCode.SYS003, HttpStatus.REQUEST_TIMEOUT,
            "요청 시간이 초과되었습니다. 다시 시도해주세요.", LogLevel.ERROR),

    INVALID_REQUEST(ApiErrorCode.VAL001, HttpStatus.BAD_REQUEST,
            "요청 데이터가 유효하지 않습니다.", LogLevel.WARN),
    MISSING_PARAMETER(ApiErrorCode.VAL002, HttpStatus.BAD_REQUEST,
            "필수 요청 파라미터가 누락되었습니다.", LogLevel.WARN),
    INVALID_PARAMETER_TYPE(ApiErrorCode.VAL003, HttpStatus.BAD_REQUEST,
            "요청 파라미터의 타입이 올바르지 않습니다.", LogLevel.WARN),

    RESOURCE_NOT_FOUND(ApiErrorCode.RES001, HttpStatus.NOT_FOUND,
            "요청한 리소스를 찾을 수 없습니다.", LogLevel.WARN),

    FILE_NOT_EXIST(ApiErrorCode.FILE001, HttpStatus.NOT_FOUND,
            "파일이 존재하지 않습니다.", LogLevel.WARN),
    FILE_NOT_EXCEL(ApiErrorCode.FILE002, HttpStatus.BAD_REQUEST,
            "올바른 엑셀 확장자가 아닙니다. .xlsx의 엑셀 파일이어야 합니다.", LogLevel.WARN),
    CELL_IS_EMPTY(ApiErrorCode.FILE003, HttpStatus.BAD_REQUEST,
            "빈 값을 가진 셀이 존재합니다.", LogLevel.WARN),
    CELL_INVALID_TYPE(ApiErrorCode.FILE004, HttpStatus.BAD_REQUEST,
            "셀의 타입이 올바르지 않습니다.", LogLevel.WARN),
    ROW_HAS_EMPTY_CELL(ApiErrorCode.FILE005, HttpStatus.BAD_REQUEST,
            "row에 빈 cell이 존재합니다.", LogLevel.WARN),
    FILE_IO_FAIL(ApiErrorCode.FILE006, HttpStatus.INTERNAL_SERVER_ERROR,
            "파일 IO에 실패했습니다.", LogLevel.ERROR),
    SKU_DUPLICATE(ApiErrorCode.FILE007, HttpStatus.CONFLICT,
            "중복된 SKU가 존재합니다.", LogLevel.WARN),
    INT_OVER_ONE(ApiErrorCode.FILE008, HttpStatus.BAD_REQUEST,
            "SKU, 구매가, 판매가, 현재 재고 수량은 0 이상이여야 합니다.", LogLevel.WARN);


    private final ApiErrorCode code;
    private final HttpStatus status;
    private final String message;
    private final LogLevel logLevel;

    ApiErrorType(ApiErrorCode code, HttpStatus status, String message, LogLevel logLevel) {
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

//    public static ApiErrorType from(ExcelErrorType excelErrorType) {
//        return switch (excelErrorType) {
//            case FILE_NOT_EXIST -> FILE_NOT_EXIST;
//            case FILE_NOT_EXCEL -> FILE_NOT_EXCEL;
//            case CELL_IS_EMPTY -> CELL_IS_EMPTY;
//            case CELL_INVALID_TYPE -> CELL_INVALID_TYPE;
//            case ROW_HAS_EMPTY_CELL -> ROW_HAS_EMPTY_CELL;
//            case FILE_IO_FAIL -> FILE_IO_FAIL;
//            case SKU_DUPLICATE -> SKU_DUPLICATE;
//            case INT_OVER_ONE -> INT_OVER_ONE;
//        };
//    }
}
