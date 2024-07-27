package com.yellobook.domain.schedule.dto.response;

import com.yellobook.domains.schedule.dto.ScheduleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "일별 일정 응답")
public class DailyScheduleDTO {
    @Schema(description = "일정 목록")
    List<ScheduleDTO> schedules;
}
