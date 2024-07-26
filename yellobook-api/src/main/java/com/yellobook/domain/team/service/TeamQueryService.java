package com.yellobook.domain.team.service;


import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domains.team.entity.Team;

public interface TeamQueryService {
    TeamResponse.InvitationCodeResponseDTO makeInvitationCode(Long teamId , TeamRequest.InvitationCodeRequestDTO request, CustomOAuth2User customOAuth2User);
    Team findByTeamId(Long teamId, Long memberId);
}
