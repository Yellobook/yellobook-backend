package com.yellobook.domains.inform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateInformRequest(
        @Schema(description = "작성할 글의 제목", example = "[공지] 공장 휴식")
        @NotBlank(message = "제목은 비워둘 수 없습니다.")
        String title,

        @Schema(description = "작성할 글의 내용", example = "개인 사정으로 하루 쉽니다.")
        String memo,

        @Schema(description = "함께 하는 멤버", example = "[1, 2, 3]")
        List<Long> mentionIds,

        @Schema(description = "날짜", example = "2024-08-15")
        @NotBlank(message = "날짜는 필수입니다.")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
        LocalDate date
) {
}
