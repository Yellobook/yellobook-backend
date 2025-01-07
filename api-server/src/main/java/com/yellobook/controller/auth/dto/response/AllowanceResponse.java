package com.yellobook.controller.auth.dto.response;

public record AllowanceResponse(
        String accessToken,
        String refreshToken
) {
}
