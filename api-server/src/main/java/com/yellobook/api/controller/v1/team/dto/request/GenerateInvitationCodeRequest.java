package com.yellobook.api.controller.v1.team.dto.request;

import com.yellobook.core.enums.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GenerateInvitationCodeRequest(
        @NotNull(message = "초대받는 상대의 권한을 설정하십시오. (VIEWER or SELLER)")
        @Schema(description = "피초대자 권한", example = "VIEWER")
        TeamMemberRole role
) {
}
