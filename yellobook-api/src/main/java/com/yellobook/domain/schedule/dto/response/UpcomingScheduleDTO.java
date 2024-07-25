package com.yellobook.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "다가오는 일정 응답")
public class UpcomingScheduleDTO {
    @Schema(description = "일정 제목")
    String scheduleTitle;
}
