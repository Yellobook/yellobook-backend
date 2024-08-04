package com.yellobook.domains.schedule.dto;

import com.yellobook.common.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class QueryScheduleDTO {
    private Long id;
    private String title;
    private LocalDate date;
    private ScheduleType scheduleType;
}

