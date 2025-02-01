package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import java.time.LocalDate;
import java.util.List;

public record NewSchedule(
        String title,
        String memo,
        LocalDate plannedDate,
        Member author,
        List<Long> memberIds
) {
}
