package com.yellobook.core.api.domains.auth.dto.response;

import lombok.Builder;

@Builder
public record AllowanceResponse(
        String accessToken,
        String refreshToken
) {
}
