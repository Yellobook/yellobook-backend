package com.yellobook.domains.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ScheduleDTO {
    private Long id;
    private String title;
    private LocalDate date;
    private String scheduleType;
}

