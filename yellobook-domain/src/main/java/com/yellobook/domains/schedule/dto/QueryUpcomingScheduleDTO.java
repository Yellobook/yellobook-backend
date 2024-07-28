package com.yellobook.domains.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class QueryUpcomingScheduleDTO {
    private String title;
    private LocalDate day;
    private String scheduleType;
}
