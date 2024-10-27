package com.yellobook.domains.auth.dto;

import com.yellobook.common.enums.TeamMemberRole;
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
