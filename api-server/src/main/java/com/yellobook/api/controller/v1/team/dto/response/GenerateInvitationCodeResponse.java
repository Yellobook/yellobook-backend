package com.yellobook.api.controller.v1.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GenerateInvitationCodeResponse(
        @Schema(description = "팀 초대 코드", example = "AG9ihkn9Ok")
        String inviteUrl
) {
}
