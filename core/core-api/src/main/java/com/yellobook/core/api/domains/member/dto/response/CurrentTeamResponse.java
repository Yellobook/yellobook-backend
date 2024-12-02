package com.yellobook.core.api.domains.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CurrentTeamResponse(
        @Schema(description = "팀 Id")
        Long teamId,

        @Schema(description = "팀 이름")
        String teamName
) {
}
