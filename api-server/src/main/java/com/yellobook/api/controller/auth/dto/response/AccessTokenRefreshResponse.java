package com.yellobook.api.controller.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "엑세스 토큰 리프레쉬 응답")
public record AccessTokenRefreshResponse(
        @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken
) {
}
