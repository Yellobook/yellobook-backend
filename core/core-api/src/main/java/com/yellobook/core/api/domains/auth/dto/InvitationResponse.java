package com.yellobook.core.api.domains.auth.dto;

import com.yellobook.core.core.enums.TeamMemberRole;
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
