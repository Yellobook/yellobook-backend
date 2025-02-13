package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;

public record Announcement(
        Long announcementId,
        String title,
        String content,
        Member author,
        int view
) {
}
