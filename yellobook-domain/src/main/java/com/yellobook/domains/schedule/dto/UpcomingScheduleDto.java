package com.yellobook.domains.schedule.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UpcomingScheduleDto {
    private String title;
    private String postType;
    private LocalDateTime createdAt;
}
