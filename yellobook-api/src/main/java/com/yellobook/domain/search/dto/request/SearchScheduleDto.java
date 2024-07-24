package com.yellobook.domain.search.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchScheduleDto {
    @Positive(message = "년도는 음수일 수 없습니다.")
    private final int year;

    @Positive(message = "월은 음수일 수 없습니다.")
    private final int month;

    @NotNull(message = "키워드는 null 일 수 없습니다.")
    private final String keyword;

    @Builder
    public SearchScheduleDto(int year, int month, String keyword) {
        this.year = year;
        this.month = month;
        this.keyword = keyword;
    }
}
