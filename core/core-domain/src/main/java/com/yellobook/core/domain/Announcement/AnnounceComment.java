package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import java.time.LocalDateTime;

public record AnnounceComment(
        Long commentId,
        String content,
        Member author,
        LocalDateTime createTime
) {
}
