package com.yellobook.api.controller.v1.team;

import com.yellobook.api.controller.v1.team.dto.request.CreateTeamRequest;
import com.yellobook.api.controller.v1.team.dto.request.GenerateInvitationCodeRequest;
import com.yellobook.api.controller.v1.team.dto.request.PatchSearchableRequest;
import com.yellobook.api.controller.v1.team.dto.response.CreateTeamResponse;
import com.yellobook.api.controller.v1.team.dto.response.GenerateInvitationCodeResponse;
import com.yellobook.api.controller.v1.team.dto.response.GetParticipantsResponse;
import com.yellobook.api.controller.v1.team.dto.response.GetTeamsResponse;
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
@RequestMapping("/api/v1/teams")
public class TeamController implements TeamApiDocs {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    @PostMapping
    public ApiResponse<CreateTeamResponse> createTeam(@RequestBody CreateTeamRequest request,
                                                      @AuthenticationPrincipal ApiMember apiMember) {
        Long teamId = teamService.create(request.toCommand(), apiMember.memberId());
        return ApiResponse.success(new CreateTeamResponse(teamId));
    }

    @Override
    @GetMapping
    public ApiResponse<GetTeamsResponse> searchPublicTeam(@RequestParam @NotBlank String name,
                                                          @AuthenticationPrincipal ApiMember apiMember) {
        List<Team> teams = teamService.searchTeamByName(name);
        return ApiResponse.success(GetTeamsResponse.from(teams));
    }

    @Override
    @PostMapping("{teamId}/apply")
    public ApiResponse<Void> requestTeamJoin(@PathVariable @NotNull Long teamId,
                                             @AuthenticationPrincipal ApiMember apiMember) {
        teamService.requestTeamJoin(teamId, apiMember.memberId());
        return ApiResponse.success(null);
    }

    @Override
    @PostMapping("{teamId}/apply/{requesterId}/accept")
    public ApiResponse<Void> acceptTeamJoinRequest(@PathVariable @NotNull Long teamId,
                                                   @PathVariable @NotNull Long requesterId,
                                                   @AuthenticationPrincipal ApiMember apiMember) {
        teamService.acceptTeamJoinRequest(teamId, requesterId, apiMember.toMember());
        return ApiResponse.success(null);
    }

    @Override
    @PostMapping("{teamId}/apply/{requesterId}/reject")
    public ApiResponse<Void> rejectTeamJoinRequest(@PathVariable @NotNull Long teamId,
                                                   @PathVariable @NotNull Long requesterId,
                                                   @AuthenticationPrincipal ApiMember apiMember) {
        teamService.rejectTeamJoinRequest(teamId, requesterId, apiMember.toMember());
        return ApiResponse.success(null);
    }

    @Override
    @PostMapping("{teamId}/invitation")
    public ApiResponse<GenerateInvitationCodeResponse> generateInvitationCode(@PathVariable @NotNull Long teamId,
                                                                              @RequestBody @Valid GenerateInvitationCodeRequest request,
                                                                              @AuthenticationPrincipal ApiMember apiMember) {
        String code = teamService.createInvitationCode(teamId, apiMember.memberId(), request.role());
        return ApiResponse.success(new GenerateInvitationCodeResponse(code));
    }

    @Override
    @PostMapping("/join")
    public ApiResponse<Void> joinTeamByCode(@RequestParam @NotBlank String code,
                                            @AuthenticationPrincipal ApiMember apiMember) {
        teamService.joinByCode(code, apiMember.memberId());
        return ApiResponse.success(null);
    }

    @Override
    @PostMapping("{teamId}/role-change")
    public ApiResponse<Void> requestOrdererConversion(@PathVariable @NotNull Long teamId,
                                                      @AuthenticationPrincipal ApiMember apiMember) {
        teamService.requestOrdererConversion(teamId, apiMember.memberId());
        return ApiResponse.success(null);
    }


    @Override
    @PostMapping("{teamId}/role-change/{requesterId}/accept")
    public ApiResponse<Void> acceptOrdererConversionRequest(@PathVariable @NotNull Long teamId,
                                                            @PathVariable @NotNull Long requesterId,
                                                            @AuthenticationPrincipal ApiMember apiMember) {
        teamService.acceptOrdererConversionRequest(teamId, requesterId, apiMember.toMember());
        return ApiResponse.success(null);
    }

    @Override
    @PostMapping("{teamId}/role-change/{requesterId}/reject")
    public ApiResponse<Void> rejectOrdererConversionRequest(@PathVariable @NotNull Long teamId,
                                                            @PathVariable @NotNull Long requesterId,
                                                            @AuthenticationPrincipal ApiMember apiMember) {
        teamService.rejectOrdererConversionRequest(teamId, requesterId, apiMember.toMember());
        return ApiResponse.success(null);
    }

    @Override
    @PatchMapping("{teamId}/searchable")
    public ApiResponse<Void> patchSearchable(@PathVariable @NotNull Long teamId,
                                             @RequestBody @Valid PatchSearchableRequest request,
                                             @AuthenticationPrincipal ApiMember apiMember) {
        teamService.updateSearchable(teamId, apiMember.memberId(), request.searchable());
        return ApiResponse.success(null);
    }

    @Override
    @DeleteMapping("{teamId}/leave")
    public ApiResponse<Void> leaveTeam(@PathVariable @NotNull Long teamId,
                                       @AuthenticationPrincipal ApiMember apiMember) {
        teamService.leaveTeam(teamId, apiMember.memberId());
        return ApiResponse.success(null);
    }

    @Override
    @GetMapping("{teamId}/participants")
    public ApiResponse<GetParticipantsResponse> getParticipants(@PathVariable @NotNull Long teamId,
                                                                @AuthenticationPrincipal ApiMember apiMember) {
        List<Participant> participants = teamService.getParticipants(teamId, apiMember.memberId());
        return ApiResponse.success(GetParticipantsResponse.from(participants));
    }


}
