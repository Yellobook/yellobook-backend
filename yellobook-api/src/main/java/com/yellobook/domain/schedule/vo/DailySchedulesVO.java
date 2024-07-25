package com.yellobook.domain.schedule.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "날짜별 일일 일정 목록 VO")
public class DailySchedulesVO {
    @Schema(description = "일")
    private Integer day;
    @Schema(description = "일정 목록")
    private List<ScheduleVO> schedules;
}
