package com.yellobook.domains.team.controller;

import com.yellobook.common.validation.annotation.ExistTeam;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.inform.dto.MentionDTO;
import com.yellobook.domains.team.dto.response.*;
import com.yellobook.domains.team.dto.request.*;
import com.yellobook.domains.team.service.TeamCommandService;
import com.yellobook.domains.team.service.TeamQueryService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
@Tag(name = "\uD83D\uDC65 팀", description = "Team API")
public class TeamController {
    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;

    @PostMapping("")
    @Operation(summary = "팀 만들기 API", description ="새로운 팀을 생성하는 API입니다.")
    public ResponseEntity<SuccessResponse<CreateTeamResponse>> createTeam(
            @RequestBody CreateTeamRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        CreateTeamResponse response = teamCommandService.createTeam(request, customOAuth2User);
        return ResponseFactory.created(response);
    }

    @PostMapping("/{teamId}/invite")
    @Operation(summary = "팀 초대하기 API", description = "팀원이 다른 구성원을 초대하기 위해 URL을 생성하는 API입니다.")
    public ResponseEntity<SuccessResponse<InvitationCodeResponse>> inviteTeam(
            @ExistTeam @PathVariable("teamId") Long teamId,
            @RequestBody InvitationCodeRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        InvitationCodeResponse response = teamQueryService.makeInvitationCode(teamId, request, customOAuth2User);
        return ResponseFactory.created(response);
    }

    @DeleteMapping("/{teamId}/leave")
    @Operation(summary = "팀 나가기 API", description = "팀원이 본인이 속한 팀에서 나가기 위한 API입니다.")
    public ResponseEntity<SuccessResponse<LeaveTeamResponse>> leaveTeam(
            @ExistTeam @PathVariable("teamId") Long teamId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        LeaveTeamResponse response = teamCommandService.leaveTeam(teamId, customOAuth2User);
        return ResponseFactory.success(response);
    }

    @GetMapping("/invitation")
    @Operation(summary = "팀 참가하기 API", description = "멤버가 팀에 참가하는 API입니다.")
    public ResponseEntity<SuccessResponse<JoinTeamResponse>> joinTeam(
            @RequestParam("code") String code,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            HttpServletResponse res
    ){
        JoinTeamResponse response = teamCommandService.joinTeam(customOAuth2User, code);
        return ResponseFactory.success(response);
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "다른 팀 불러오기 API", description = "멤버가 다른 팀의 정보를 가지고 오는 API입니다.")
    public ResponseEntity<SuccessResponse<GetTeamResponse>> getTeam(
            @ExistTeam @PathVariable("teamId") Long teamId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        GetTeamResponse response = teamQueryService.findByTeamId(teamId, customOAuth2User);
        return ResponseFactory.success(response);
    }

    @GetMapping("/{teamId}/search")
    @Operation(summary = "팀 내의 멤버 검색하기 API", description = "팀 내의 멤버들을 검색하는 API입니다.")
    public ResponseEntity<SuccessResponse<List<MentionDTO>>> searchMembers(
            @PathVariable Long teamId,
            @RequestParam String name
    ){
        List<MentionDTO> response = teamQueryService.searchParticipants(teamId, name);
        return ResponseFactory.success(response);
    }
}
