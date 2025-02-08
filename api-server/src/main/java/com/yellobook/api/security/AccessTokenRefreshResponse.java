package com.yellobook.api.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "엑세스 토큰 리프레쉬 응답")
public record AccessTokenRefreshResponse(
        @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken
) {
    public static AccessTokenRefreshResponse of(String accessToken) {
        return new AccessTokenRefreshResponse(accessToken);
    }
}
