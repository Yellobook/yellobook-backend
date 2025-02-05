package com.yellobook.core.domain.inform;

import com.yellobook.core.domain.member.Member;
import java.time.LocalDate;

public record Inform(
        Long informId,
        Member author,
        String title,
        String content,
        int view,
        LocalDate scheduledDate
) {
}
