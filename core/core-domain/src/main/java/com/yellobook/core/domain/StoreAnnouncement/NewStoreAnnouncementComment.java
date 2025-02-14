package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;

public record NewStoreAnnouncementComment(
        String content,
        Member member,
        Long announcementId
) {
}
