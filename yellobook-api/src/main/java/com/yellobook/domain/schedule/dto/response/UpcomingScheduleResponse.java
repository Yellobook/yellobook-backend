package com.yellobook.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "다가오는 일정 응답")
public record UpcomingScheduleResponse(
        @Schema(description = "일정 제목")
        String scheduleTitle
) {
}
