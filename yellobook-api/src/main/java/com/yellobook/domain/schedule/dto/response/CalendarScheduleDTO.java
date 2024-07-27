package com.yellobook.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(description = "월별 종합 일정 응답")
public class CalendarScheduleDTO {
    @Schema(description = "날짜별 일정 목록")
    List<CalendarItemDTO> calendar;
}
