package com.yellobook.excel;

public enum ExcelErrorType {
    FILE_NOT_EXIST("파일이 존재하지 않습니다."),
    FILE_NOT_EXCEL("올바른 엑셀 확장자가 아닙니다. .xlsx의 엑셀 파일이어야 합니다."),
    CELL_IS_EMPTY("빈 값을 가진 셀이 존재합니다."),
    CELL_INVALID_TYPE("셀의 타입이 올바르지 않습니다."),
    ROW_HAS_EMPTY_CELL("row에 빈 cell이 존재합니다."),
    FILE_IO_FAIL("파일 IO에 실패했습니다."),
    SKU_DUPLICATE("중복된 SKU가 존재합니다."),
    INT_OVER_ONE("SKU, 구매가, 판매가, 현재 재고 수량은 0 이상이여야 합니다."),
    ;

    private final String message;


    ExcelErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
