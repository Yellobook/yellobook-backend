package com.yellobook.api.controller.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentTeamResponse(
        @Schema(description = "팀 Id")
        Long teamId,

        @Schema(description = "팀 이름")
        String teamName
) {
}
