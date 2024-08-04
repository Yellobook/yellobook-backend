package com.yellobook.domain.team.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;

public interface TeamCommandService {
    TeamResponse.CreateTeamResponseDTO createTeam(TeamRequest.CreateTeamRequestDTO request, CustomOAuth2User customOAuth2User);
    TeamResponse.LeaveTeamResponseDTO leaveTeam(Long teamId, CustomOAuth2User customOAuth2User);
    TeamResponse.JoinTeamResponseDTO joinTeam(CustomOAuth2User customOAuth2User, String code);
}
