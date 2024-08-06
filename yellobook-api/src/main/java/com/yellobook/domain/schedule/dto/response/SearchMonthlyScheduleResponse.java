package com.yellobook.domain.schedule.dto.response;

import com.yellobook.domains.schedule.dto.query.QuerySchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.util.List;

@Builder
@Schema(description = "월별 키워드에 해당하는 일정 응답")
public record SearchMonthlyScheduleResponse(
        @Schema(description = "일정 목록")
        List<QuerySchedule> schedules
) {
}