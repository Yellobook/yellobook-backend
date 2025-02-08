package com.yellobook.admin.support.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yellobook.admin.support.error.AdminErrorMessage;
import com.yellobook.admin.support.error.AdminErrorType;

public class AdminResponse<S> {
    private final ResultType result;

    private final S data;

    @JsonInclude(Include.NON_EMPTY)
    private final AdminErrorMessage error;

    private AdminResponse(ResultType result, S data, AdminErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static AdminResponse<?> success() {
        return new AdminResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> AdminResponse<S> success(S data) {
        return new AdminResponse<>(ResultType.SUCCESS, data, null);
    }

    public static AdminResponse<?> error(AdminErrorType error) {
        return new AdminResponse<>(ResultType.ERROR, null, new AdminErrorMessage(error));
    }

    public static AdminResponse<?> error(AdminErrorType error, Object errorData) {
        return new AdminResponse<>(ResultType.ERROR, null, new AdminErrorMessage(error, errorData));
    }

    public ResultType getResult() {
        return result;
    }

    public Object getData() {
        return data;
    }

    public AdminErrorMessage getError() {
        return error;
    }
}