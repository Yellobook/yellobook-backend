package com.yellobook.domain.schedule.dto.response;

import com.yellobook.domain.schedule.vo.DailySchedulesVO;
import com.yellobook.domain.schedule.vo.ScheduleVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(description = "월별 키워드에 해당하는 일정 응답")
public class SearchMonthlyScheduleDTO {
    @Schema(description = "일정 목록")
    List<ScheduleVO> schedules;
}
