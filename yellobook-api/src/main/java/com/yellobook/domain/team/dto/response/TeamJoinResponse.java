package com.yellobook.domain.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TeamJoinResponse(
        @Schema(description = "합류한 팀의 고유 id", example = "12345")
        Long teamId
) {
}
