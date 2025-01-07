package com.yellobook.core.domain.member;

public record Member(
        Long memberId,
        String email,
        String nickname,
        String profileImage,
        Boolean allowance
) {
}
