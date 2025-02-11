package com.yellobook.api.support.response;

import com.yellobook.api.security.error.AuthErrorType;
import com.yellobook.api.support.error.ApiErrorMessage;
import com.yellobook.api.support.error.ApiErrorType;
import com.yellobook.core.error.CoreErrorType;
import io.swagger.v3.oas.annotations.media.Schema;

public class ApiResponse<S> {
    @Schema(description = "응답 결과 상태 (SUCCESS 또는 ERROR)", example = "SUCCESS")
    private final ResultType result;

    @Schema(description = "성공 시 반환되는 데이터")
    private final S data;

    @Schema(description = "에러 발생 시 에러 메시지")
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

    public static ApiResponse<?> error(AuthErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ApiErrorMessage(error));
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