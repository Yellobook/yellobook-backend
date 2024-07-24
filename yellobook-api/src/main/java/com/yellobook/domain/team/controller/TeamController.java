package com.yellobook.domain.team.controller;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domain.team.service.TeamCommandService;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
@Tag(name = "\uD83D\uDC65 팀", description = "Team API")
public class TeamController {
    private final TeamCommandService teamCommandService;

    @PostMapping("/")
    @Operation(summary = "팀 만들기 API", description ="새로운 팀을 생성하는 API입니다.")
    public ResponseEntity<SuccessResponse<TeamResponse.CreateTeamResponseDTO>> createTeam(
            @RequestBody TeamRequest.CreateTeamRequestDTO request
    ) {
        Team team = teamCommandService.createTeam(request);
        return null;
    }

    @PostMapping("/{teamId}/invite")
    @Operation(summary = "팀 초대하기 API", description = "팀원이 다른 구성원을 초대하기 위해 URL을 생성하는 API입니다.")
    public ResponseEntity<SuccessResponse<TeamResponse.InviteTeamResponseDTO>> inviteTeam(
            @PathVariable Long teamId,
            @RequestBody TeamRequest.InviteTeamRequestDTO request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        teamCommandService.inviteTeam(request);
        return null;
    }
}
