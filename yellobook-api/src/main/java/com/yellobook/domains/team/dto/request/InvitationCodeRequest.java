package com.yellobook.domains.team.dto.request;

import com.yellobook.common.enums.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record InvitationCodeRequest(
        @NotNull(message = "초대받는 상대의 권한을 설정하십시오.")
        @Schema(description = "피초대자 권한", example = "VIEWER")
        TeamMemberRole role
) {
}
