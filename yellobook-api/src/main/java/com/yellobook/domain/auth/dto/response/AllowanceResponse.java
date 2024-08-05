package com.yellobook.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record AllowanceResponse(
        String accessToken,
        String refreshToken
) {
}
