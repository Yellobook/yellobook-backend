package com.yellobook.api.controller.v1.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateScheduleResponse(
        @Schema(description = "생성된 일정의 id", example = "123")
        Long scheduleId
) {
}
