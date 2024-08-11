package com.yellobook.domains.schedule.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springdoc.core.annotations.ParameterObject;

@Builder
@ParameterObject
@Schema(description = "월별 일정 조회를 위한 파라미터")
public record MonthlyParam(
        @Min(value = 0, message = "유효한 년도를 입력해 주세요.")
        @Parameter(description = "년", example = "2024")
        int year,

        @Min(value = 1, message = "유효한 월을 입력해 주세요.")
        @Max(value = 12, message = "유효한 월을 입력해 주세요.")
        @Parameter(description = "월", example = "7")
        int month
) {
}

