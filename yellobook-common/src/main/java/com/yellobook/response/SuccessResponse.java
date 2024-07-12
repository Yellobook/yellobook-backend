package com.yellobook.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({ "isSuccess", "message", "data" })
public class SuccessResponse<T> extends BaseResponse {
    private final T data;

    @Builder(access = AccessLevel.PROTECTED)
    private SuccessResponse(Boolean isSuccess, String message, T data) {
        super(isSuccess, message);
        this.data = data;
    }
}
