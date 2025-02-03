package com.yellobook.admin.support.error;

public class AdminException extends RuntimeException {
    private final AdminErrorType errorType;

    private final Object data;

    public AdminException(AdminErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public AdminException(AdminErrorType errorType, Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }

    public AdminErrorType getErrorType() {
        return errorType;
    }

    public Object getData() {
        return data;
    }
}