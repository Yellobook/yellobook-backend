package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;
import java.time.LocalDateTime;

public record StoreAnnouncementComment(
        Long commentId,
        String content,
        Member author,
        LocalDateTime createTime
) {
}
