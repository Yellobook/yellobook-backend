package com.yellobook.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@JsonPropertyOrder({ "isSuccess", "code", "message", "errors" })
public class ErrorResponse extends BaseResponse {
    @Schema(name = "에러 코드")
    private final String code;

    @Schema(name = "에러 필드 목록")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Builder
    private ErrorResponse(String code, String message, List<ValidationError> errors) {
        super(false, message);
        this.code = code;
        this.errors = errors;
    }
}
