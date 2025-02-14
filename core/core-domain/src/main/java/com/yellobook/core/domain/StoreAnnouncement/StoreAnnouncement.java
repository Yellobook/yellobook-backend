package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;

public record StoreAnnouncement(
        Long announcementId,
        String title,
        String content,
        Member author,
        int view
) {
}
