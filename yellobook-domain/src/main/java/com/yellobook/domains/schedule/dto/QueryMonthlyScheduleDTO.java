package com.yellobook.domains.schedule.dto;

import com.yellobook.common.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class QueryMonthlyScheduleDTO {
    private Long id;
    private String title;
    private LocalDate date;
    private LocalDateTime createdAt;
    private ScheduleType scheduleType;
}
