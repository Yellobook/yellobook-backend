package com.yellobook.domain.team.service;

import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domains.team.entity.Team;

public interface TeamCommandService {
    Team createTeam(TeamRequest.CreateTeamRequestDTO request);
    String inviteTeam(TeamRequest.InviteTeamRequestDTO request);
    Boolean leaveTeam(Long teamId, Long participantId);
}
