package com.yellobook.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({ "isSuccess", "code", "message", "errors" })
public class ErrorResponse extends BaseResponse {
    private final String code;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Builder(access = AccessLevel.PROTECTED)
    private ErrorResponse(String code, String message, List<ValidationError> errors) {
        super(false, message);
        this.code = code;
        this.errors = errors;
    }
}
