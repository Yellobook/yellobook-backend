package com.yellobook.support.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess", "message", "data"})
@Schema(description = "성공 응답")
public class SuccessResponse<T> extends BaseResponse {
    @Schema(description = "응답 데이터")
    private final T data;

    @Builder(access = AccessLevel.PROTECTED)
    private SuccessResponse(Boolean isSuccess, String message, T data) {
        super(isSuccess, message);
        this.data = data;
    }
}
