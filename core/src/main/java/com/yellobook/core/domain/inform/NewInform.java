package com.yellobook.core.domain.inform;

import com.yellobook.core.domain.member.Member;
import java.time.LocalDate;
import java.util.List;

public record NewInform(
        String title,
        String memo,
        LocalDate plannedDate,
        Member author,
        List<Long> memberIds
) {
}
