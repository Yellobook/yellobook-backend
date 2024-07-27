package com.yellobook.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Schema(description = "캘린더 일별 일정목록")
public class CalendarItemDTO {
    @Schema(description = "일자")
    Integer day;
    @Schema(description = "일정 제목 목록")
    List<String> titles;

    @Builder
    private CalendarItemDTO(
            @NonNull  Integer day,
            @NonNull  List<String> titles
    ) {
        this.day = day;
        this.titles = titles;
    }
}
