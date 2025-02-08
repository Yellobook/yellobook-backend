package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import java.time.LocalDateTime;

public record ScheduleComment(
        Long commentId,
        Member commenter,
        String content,
        LocalDateTime createTime
) {
}
