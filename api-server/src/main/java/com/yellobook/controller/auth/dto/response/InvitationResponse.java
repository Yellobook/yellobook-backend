package com.yellobook.controller.auth.dto.response;

import com.yellobook.TeamMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InvitationResponse {
    Long teamId;
    TeamMemberRole role;
}
