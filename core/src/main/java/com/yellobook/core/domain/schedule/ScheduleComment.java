package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;

public record ScheduleComment(
        Long commentId,
        Member commenter,
        String content
) {
}
