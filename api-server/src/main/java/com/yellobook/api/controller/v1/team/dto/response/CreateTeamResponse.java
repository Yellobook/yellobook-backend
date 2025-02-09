package com.yellobook.api.controller.v1.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTeamResponse(
        @Schema(description = "생성된 팀의 고유 id", example = "123")
        Long teamId
) {
}
