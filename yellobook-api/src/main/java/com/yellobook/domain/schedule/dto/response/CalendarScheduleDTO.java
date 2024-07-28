package com.yellobook.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "월별 종합 일정 응답")
public class CalendarScheduleDTO {
    @Schema(description = "날짜별 일정 목록")
    private final List<CalendarItemDTO> calendar;

    // CalendarScheduleDTO 에만 관련이 있고, 외부에서 접근할 필요가 없으므로 내부클래스로 정의
    @Getter
    @Schema(description = "캘린더 일별 일정목록")
    public static class CalendarItemDTO {
        @Schema(description = "일자")
        private final Integer day;
        @Schema(description = "일정 제목 목록")
        private final List<String> titles;

        @Builder
        private CalendarItemDTO(
                @NonNull Integer day,
                @NonNull List<String> titles
        ) {
            this.day = day;
            this.titles = titles;
        }
    }
}
