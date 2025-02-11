package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;

public record NewAnnouncementComment(
        String content,
        Member member,
        Long announcementId
) {
}
