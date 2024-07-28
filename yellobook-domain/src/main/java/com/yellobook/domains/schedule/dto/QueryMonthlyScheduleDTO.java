package com.yellobook.domains.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class QueryMonthlyScheduleDTO {
    private Long id;
    private String title;
    private LocalDate date;
    private LocalDateTime createdAt;
    private String scheduleType;
}
