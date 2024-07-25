package com.yellobook.domain.schedule.dto.response;

import com.yellobook.domain.schedule.vo.DailySchedulesVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "월별 종합 일정 응답")
public class MonthlyTotalScheduleDTO {
    @Schema(description = "날짜별 일정 목록")
    List<DailySchedulesVO> calendar;
}
