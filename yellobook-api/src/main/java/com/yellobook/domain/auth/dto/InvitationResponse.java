package com.yellobook.domain.auth.dto;

import com.yellobook.enums.MemberTeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InvitationResponse {
    Long teamId;
    MemberTeamRole role;
}
