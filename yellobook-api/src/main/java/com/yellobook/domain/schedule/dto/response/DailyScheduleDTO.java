package com.yellobook.domain.schedule.dto.response;

import com.yellobook.domains.schedule.dto.QueryScheduleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "일별 일정 응답")
public class DailyScheduleDTO {
    @Schema(description = "일정 목록")
    private final List<QueryScheduleDTO> schedules;
}
