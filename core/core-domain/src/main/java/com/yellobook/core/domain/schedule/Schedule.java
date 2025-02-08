package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import java.time.LocalDate;

public record Schedule(
        Long scheduleId,
        Member author,
        String title,
        String content,
        int view,
        LocalDate scheduledDate
) {
}
