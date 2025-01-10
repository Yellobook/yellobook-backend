package com.yellobook.api.support.error;

public class ApiException extends RuntimeException {
    private final ApiErrorType errorType;

    private final Object data;

    public ApiException(ApiErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public ApiException(ApiErrorType errorType, Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }

    public ApiErrorType getErrorType() {
        return errorType;
    }

    public Object getData() {
        return data;
    }
}