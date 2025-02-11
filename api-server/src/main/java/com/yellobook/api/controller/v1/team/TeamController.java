package com.yellobook.api.controller.v1.team;

import com.yellobook.api.controller.v1.team.dto.request.ApproveRejectRequest;
import com.yellobook.api.controller.v1.team.dto.request.CreateTeamRequest;
import com.yellobook.api.controller.v1.team.dto.request.GenerateInvitationCodeRequest;
import com.yellobook.api.controller.v1.team.dto.request.PatchSearchableRequest;
import com.yellobook.api.controller.v1.team.dto.response.CreateTeamResponse;
import com.yellobook.api.controller.v1.team.dto.response.GenerateInvitationCodeResponse;
import com.yellobook.api.controller.v1.team.dto.response.GetParticipantsResponse;
import com.yellobook.api.controller.v1.team.dto.response.GetTeamsResponse;
import com.yellobook.api.controller.v1.team.dto.response.GetTeamsResponse.GetTeamResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.core.domain.team.Participant;
import com.yellobook.core.domain.team.Team;
import com.yellobook.core.domain.team.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/stores")
public class TeamController implements TeamApiDocs {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ApiResponse<CreateTeamResponse> createTeam(@RequestBody CreateTeamRequest dto,
                                                      @AuthenticationPrincipal ApiMember apiMember) {
        Long storeId = teamService.create(dto.toCommand(), apiMember.memberId());
        return ApiResponse.success(new CreateTeamResponse(storeId));
    }

    @GetMapping
    public ApiResponse<GetTeamsResponse> searchPublicTeam(@RequestParam @NotBlank String name,
                                                          @AuthenticationPrincipal ApiMember apiMember) {
        List<Team> teams = teamService.searchTeamByName(name);
        return ApiResponse.success(GetTeamsResponse.from(teams));
    }

    @PostMapping("/join")
    public ApiResponse<Void> joinTeamByCode(@RequestParam @NotBlank String code,
                                            @AuthenticationPrincipal ApiMember apiMember) {
        teamService.joinByCode(code, apiMember.memberId());
        return ApiResponse.success(null);
    }

    @GetMapping("/{storeId}")
    public ApiResponse<GetTeamResponse> getStore(@PathVariable @NotNull Long storeId,
                                                 @AuthenticationPrincipal ApiMember apiMember) {
        Team team = teamService.getTeam(storeId, apiMember.memberId());
        return ApiResponse.success(new GetTeamResponse(team));
    }

    @PatchMapping("/{storeId}/searchable")
    public ApiResponse<Void> patchSearchable(@PathVariable @NotNull Long storeId,
                                             @RequestBody @Valid PatchSearchableRequest dto,
                                             @AuthenticationPrincipal ApiMember apiMember) {
        teamService.updateSearchable(storeId, apiMember.memberId(), dto.searchable());
        return ApiResponse.success(null);
    }

    @PostMapping("/{storeId}/join")
    public ApiResponse<Void> requestTeamJoin(@PathVariable @NotNull Long storeId,
                                             @AuthenticationPrincipal ApiMember apiMember) {
        teamService.requestTeamJoin(storeId, apiMember.memberId());
        return ApiResponse.success(null);
    }

    @PatchMapping("/{storeId}/join/members/{memberId}")
    public ApiResponse<Void> manageTeamJoinRequest(@PathVariable @NotNull Long storeId,
                                                   @PathVariable @NotNull Long memberId,
                                                   @RequestBody ApproveRejectRequest dto,
                                                   @AuthenticationPrincipal ApiMember apiMember) {
        teamService.manageTeamJoinRequest(storeId, memberId, apiMember.toMember(), dto.approve());
        return ApiResponse.success(null);
    }

    @PostMapping("/{storeId}/invite")
    public ApiResponse<GenerateInvitationCodeResponse> generateInvitationCode(@PathVariable @NotNull Long storeId,
                                                                              @RequestBody @Valid GenerateInvitationCodeRequest request,
                                                                              @AuthenticationPrincipal ApiMember apiMember) {
        String code = teamService.createInvitationCode(storeId, apiMember.memberId(), request.role());
        return ApiResponse.success(new GenerateInvitationCodeResponse(code));
    }

    @DeleteMapping("/{storeId}/leave")
    public ApiResponse<Void> leaveTeam(@PathVariable @NotNull Long storeId,
                                       @AuthenticationPrincipal ApiMember apiMember) {
        teamService.leaveTeam(storeId, apiMember.memberId());
        return ApiResponse.success(null);
    }

    @GetMapping("/{storeId}/members")
    public ApiResponse<GetParticipantsResponse> getParticipants(@PathVariable @NotNull Long storeId,
                                                                @AuthenticationPrincipal ApiMember apiMember) {
        List<Participant> participants = teamService.getParticipants(storeId, apiMember.memberId());
        return ApiResponse.success(GetParticipantsResponse.from(participants));
    }

    @PostMapping("/{storeId}/members/role/orderer")
    public ApiResponse<Void> requestOrdererConversion(@PathVariable @NotNull Long storeId,
                                                      @AuthenticationPrincipal ApiMember apiMember) {
        teamService.requestOrdererConversion(storeId, apiMember.memberId());
        return ApiResponse.success(null);
    }

    @PatchMapping("/{storeId}/members/{memberId}/role/orderer")
    public ApiResponse<Void> changeRoleToOrderer(@PathVariable @NotNull Long storeId,
                                                 @PathVariable @NotNull Long memberId,
                                                 @RequestBody ApproveRejectRequest dto,
                                                 @AuthenticationPrincipal ApiMember apiMember) {
        teamService.changeRoleToOrderer(storeId, memberId, apiMember.toMember(), dto.approve());
        return ApiResponse.success(null);
    }

}
