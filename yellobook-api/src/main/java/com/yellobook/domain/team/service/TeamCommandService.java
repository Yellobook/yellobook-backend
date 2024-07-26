package com.yellobook.domain.team.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;

public interface TeamCommandService {
    TeamResponse.CreateTeamResponseDTO createTeam(TeamRequest.CreateTeamRequestDTO request, CustomOAuth2User customOAuth2User);
    String inviteTeam(TeamRequest.InviteTeamRequestDTO request);
    TeamResponse.LeaveTeamResponseDTO leaveTeam(Long teamId, CustomOAuth2User customOAuth2User);
    TeamResponse.JoinTeamResponseDTO joinTeam(Long teamId, TeamRequest.JoinTeamRequestDTO request);
}
