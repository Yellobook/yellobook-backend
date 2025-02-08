package com.yellobook.core.domain.schedule;

public record NewScheduleComment(
        Long commenterId,
        Long scheduleId,
        String content
) {
}
