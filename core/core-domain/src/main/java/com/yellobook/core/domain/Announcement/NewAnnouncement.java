package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;

public record NewAnnouncement(
        String title,
        String content,
        Long teamId,
        Member author
) {
}
