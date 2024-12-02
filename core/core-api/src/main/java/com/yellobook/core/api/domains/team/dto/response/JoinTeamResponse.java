package com.yellobook.core.api.domains.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record JoinTeamResponse(
        @Schema(description = "합류한 팀의 고유 id", example = "12345")
        Long teamId
) {
}
