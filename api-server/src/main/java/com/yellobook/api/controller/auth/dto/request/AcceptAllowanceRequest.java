package com.yellobook.api.controller.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "약관 동의 요청")
public record AcceptAllowanceRequest(
        @NotBlank(message = "약관 동의 토큰이 존재하지 않습니다.")
        @Schema(description = "약관 동의 토큰")
        String token
) {
}


