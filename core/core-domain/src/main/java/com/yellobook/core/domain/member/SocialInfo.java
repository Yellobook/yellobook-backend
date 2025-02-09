package com.yellobook.core.domain.member;

public record SocialInfo(
        String oauthId,
        String provider,
        String email
) {
}
