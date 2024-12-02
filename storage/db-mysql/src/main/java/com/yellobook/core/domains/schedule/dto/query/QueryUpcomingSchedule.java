package com.yellobook.core.domains.schedule.dto.query;

import com.yellobook.core.core.enums.ScheduleType;

import java.time.LocalDate;

public record QueryUpcomingSchedule(
        String title,
        LocalDate day,
        ScheduleType scheduleType
) {
}
