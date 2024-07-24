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

    @DeleteMapping("{teamId}/leave")
    @Operation(summary = "팀 나가기 API", description = "팀원이 본인이 속한 팀에서 나가기 위한 API입니다.")
    public ResponseEntity<SuccessResponse<TeamResponse.LeaveTeamResponseDTO>> leaveTeam(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }

    @PostMapping("/{teamId}/join")
    @Operation(summary = "팀 참가하기 API", description = "멤버가 팀에 참가하는 API입니다.")
    public ResponseEntity<SuccessResponse<TeamResponse.JoinTeamResponseDTO>> joinTeam(
            @PathVariable Long teamId,
            @RequestBody TeamRequest.JoinTeamRequestDTO request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "다른 팀 불러오기 API", description = "멤버가 다른 팀의 정보를 가지고 오는 API입니다.")
    public ResponseEntity<SuccessResponse<TeamResponse.GetTeamResponseDTO>> getTeam(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        return null;
    }
}
