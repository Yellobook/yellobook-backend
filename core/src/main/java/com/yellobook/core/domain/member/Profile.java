package com.yellobook.core.domain.member;

public record Profile(
        String email,
        String nickname,
        String profileImage
) {
}
