package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;

public record NewStoreAnnouncement(
        String title,
        String content,
        Long teamId,
        Member author
) {
}
