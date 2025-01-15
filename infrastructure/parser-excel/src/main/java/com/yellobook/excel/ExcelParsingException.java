package com.yellobook.excel;

public class ExcelParsingException extends RuntimeException {
    private final ExcelErrorType errorType;
    private final Object data;

    public ExcelParsingException(ExcelErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public ExcelParsingException(ExcelErrorType errorType, Object object) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = object;
    }

    public ExcelErrorType getErrorType() {
        return errorType;
    }

    public Object getData() {
        return data;
    }
}
