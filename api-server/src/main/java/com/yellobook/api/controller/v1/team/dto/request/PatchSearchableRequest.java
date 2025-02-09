package com.yellobook.api.controller.v1.team.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PatchSearchableRequest(
        @NotNull(message = "팀의 공개 여부는 필수 입력 사항입니다.")
        @Schema(description = "공개 여부", example = "true")
        Boolean searchable
) {
}
