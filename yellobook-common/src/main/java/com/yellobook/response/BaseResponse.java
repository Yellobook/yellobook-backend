package com.yellobook.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseResponse {
    @JsonProperty("isSuccess")
    @Schema(description = "요청이 성공했는지 여부", example = "true")
    Boolean isSuccess;

    @JsonProperty("message")
    @Schema(description = "추가 정보를 제공하는 메시지", example = "요청이 성공적으로 처리되었습니다.")
    String message;
}
