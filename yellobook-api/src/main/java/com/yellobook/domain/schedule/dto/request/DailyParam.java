package com.yellobook.domain.schedule.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@RequiredArgsConstructor
@ParameterObject
@Schema(description = "일일 일정 조회를 위한 파라미터")
public class DailyParam {
    @Parameter(description = "년", example = "2024")
    private final int year;

    @Parameter(description = "월", example = "07")
    private final int month;

    @Parameter(description = "일", example = "24")
    private final int day;
}
