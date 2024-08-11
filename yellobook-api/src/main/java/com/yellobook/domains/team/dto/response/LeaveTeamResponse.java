package com.yellobook.domains.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record LeaveTeamResponse(
        @Schema(description = "나간 팀의 고유 id", example = "1234")
        Long teamId
) {
}
