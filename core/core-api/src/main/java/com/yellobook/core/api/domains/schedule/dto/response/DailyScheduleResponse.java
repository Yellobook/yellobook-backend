package com.yellobook.core.api.domains.schedule.dto.response;

import com.yellobook.core.domains.schedule.dto.query.QuerySchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "일별 일정 응답")
public record DailyScheduleResponse(
        @Schema(description = "일정 목록")
        List<QuerySchedule> schedules
) {
}
