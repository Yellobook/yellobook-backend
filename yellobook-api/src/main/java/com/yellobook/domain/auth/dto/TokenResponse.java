package com.yellobook.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {
    String accessToken;

    @Builder
    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
    }
}
