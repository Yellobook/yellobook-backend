package com.yellobook.api.support.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yellobook.api.support.error.ApiErrorType;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.api.support.error.ApiErrorMessage;

public class ApiResponse<S> {
    private final ResultType result;

    private final S data;

    @JsonInclude(Include.NON_EMPTY)
    private final ApiErrorMessage error;

    private ApiResponse(ResultType result, S data, ApiErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(ApiErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ApiErrorMessage(error));
    }

    public static ApiResponse<?> error(ApiErrorType error, Object errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ApiErrorMessage(error, errorData));
    }

    public static ApiResponse<?> error(CoreErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ApiErrorMessage(error));
    }

    public static ApiResponse<?> error(CoreErrorType error, Object errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ApiErrorMessage(error, errorData));
    }

    public ResultType getResult() {
        return result;
    }

    public Object getData() {
        return data;
    }

    public ApiErrorMessage getError() {
        return error;
    }
}