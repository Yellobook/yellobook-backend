package com.yellobook.domain.schedule.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springdoc.core.annotations.ParameterObject;

@Builder
@ParameterObject
@Schema(description = "월 일정 조회를 위한 파라미터")
public record MonthlySearchParam(
        @NotBlank(message = "년도를 입력해 주세요.")
        @Parameter(description = "년", example = "2024")
        int year,
        @NotBlank(message = "월을 입력해 주세요.")
        @Parameter(description = "월", example = "7")
        int month,
        @Parameter(description = "검색 키워드", example = "근무")
        String keyword
) {
}
