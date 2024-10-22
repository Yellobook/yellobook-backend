package com.yellobook.domains.schedule.dto.query;

import com.yellobook.common.enums.ScheduleType;
import java.time.LocalDate;

public record QueryUpcomingSchedule(
        String title,
        LocalDate day,
        ScheduleType scheduleType
) {
}
