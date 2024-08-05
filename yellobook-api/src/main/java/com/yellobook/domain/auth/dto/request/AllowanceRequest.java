package com.yellobook.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "약관 동의 요청")
public class AllowanceRequest {
    @NotBlank(message = "약관 동의 토큰이 존재하지 않습니다.")
    @Schema(description = "약관 동의 토큰")
    private String token;
}
