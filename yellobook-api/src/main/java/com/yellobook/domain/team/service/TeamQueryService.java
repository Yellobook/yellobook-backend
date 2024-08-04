package com.yellobook.domain.team.service;


import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domain.inform.dto.MentionDTO;

import java.util.List;

public interface TeamQueryService {
    TeamResponse.InvitationCodeResponseDTO makeInvitationCode(Long teamId , TeamRequest.InvitationCodeRequestDTO request, CustomOAuth2User customOAuth2User);
    TeamResponse.GetTeamResponseDTO findByTeamId(Long teamId, CustomOAuth2User customOAuth2User);
    boolean existsByTeamId(Long teamId);
    List<MentionDTO> searchParticipants(Long teamId, String name);
}
