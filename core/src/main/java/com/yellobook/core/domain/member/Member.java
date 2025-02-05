package com.yellobook.core.domain.member;

public record Member(
        Long memberId,
        ProfileInfo profileInfo,
        SocialInfo socialInfo
) {
}
