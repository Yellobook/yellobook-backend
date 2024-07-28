package com.yellobook.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "다가오는 일정 응답")
public class UpcomingScheduleDTO {
    @Schema(description = "일정 제목")
    private final String scheduleTitle;
}
