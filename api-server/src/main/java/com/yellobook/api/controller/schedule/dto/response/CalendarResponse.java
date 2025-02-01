package com.yellobook.api.controller.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "월별 종합 일정 응답")
public record CalendarResponse(
        @Schema(description = "날짜별 일정 목록")
        List<CalendarItem> calendar
) {
}
