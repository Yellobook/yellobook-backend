package com.yellobook.domains.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DailyScheduleDto {
    private LocalDate date;
    private List<ScheduleDto> schedules;
}
