package com.yellobook.domains.schedule.dto.response;

import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "월별 키워드에 해당하는 일정 응답")
public record SearchMonthlyScheduleResponse(
        @Schema(description = "일정 목록")
        List<QuerySchedule> schedules
) {
}