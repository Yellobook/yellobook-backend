package com.yellobook.controller.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "캘린더 일별 일정목록")
public record CalendarItem(
        @Schema(description = "일자")
        Integer day,

        @Schema(description = "일정 제목 목록")
        List<String> titles
) {
}
