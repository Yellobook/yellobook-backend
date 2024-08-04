package com.yellobook.domains.schedule.dto;

import com.yellobook.common.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class QueryUpcomingScheduleDTO {
    private String title;
    private LocalDate day;
    private ScheduleType scheduleType;
}
