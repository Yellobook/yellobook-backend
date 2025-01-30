package com.yellobook.api.controller.auth.dto.request;

public record AccessTokenRefreshRequest(
        String refreshToken
) {
}
