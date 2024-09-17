package com.yellobook.domains.schedule.dto.query;

import com.yellobook.common.enums.ScheduleType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record QueryMonthlySchedule(
        Long id,
        String title,
        LocalDate date,
        LocalDateTime createdAt,
        ScheduleType scheduleType
) {
}
