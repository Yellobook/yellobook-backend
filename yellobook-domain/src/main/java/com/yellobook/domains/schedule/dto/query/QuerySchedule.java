package com.yellobook.domains.schedule.dto.query;

import com.yellobook.common.enums.ScheduleType;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record QuerySchedule(
        Long id,
        String title,
        LocalDate date,
        ScheduleType scheduleType
) {
}

