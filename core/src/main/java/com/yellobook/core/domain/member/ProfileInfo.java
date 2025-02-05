package com.yellobook.core.domain.member;

import java.time.LocalDateTime;

public record ProfileInfo(
        String nickname,
        String bio,
        String profileImage,
        LocalDateTime nicknameUpdatedAt
) {
}
