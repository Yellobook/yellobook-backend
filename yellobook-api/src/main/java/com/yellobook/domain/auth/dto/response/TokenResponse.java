package com.yellobook.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "토큰 응답")
public class TokenResponse {
    @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private final String accessToken;

    @Builder
    private TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
