package com.yellobook.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "약관 동의 요청")
public class AllowanceRequest {
    @Schema(description = "약관 동의 토큰")
    private String token;
}
