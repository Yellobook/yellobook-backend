package com.yellobook.domains.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "월별 종합 일정 응답")
public record CalendarResponse(
        @Schema(description = "날짜별 일정 목록")
        List<CalendarItem> calendar
) {
}
