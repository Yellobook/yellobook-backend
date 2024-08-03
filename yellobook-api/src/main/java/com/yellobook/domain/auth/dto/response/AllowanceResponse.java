package com.yellobook.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "약관 동의 후 토큰 응답")
public class AllowanceResponse {
    @Schema(description = "accessToken")
    private final String accessToken;

    @Schema(description = "refreshToken")
    private final String refreshToken;

    @Builder
    private AllowanceResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
