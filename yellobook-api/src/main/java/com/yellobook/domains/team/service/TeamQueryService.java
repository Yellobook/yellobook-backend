package com.yellobook.domains.team.service;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.dto.request.InvitationCodeRequest;
import com.yellobook.domains.team.dto.response.GetTeamResponse;
import com.yellobook.domains.team.dto.response.InvitationCodeResponse;
import com.yellobook.domains.team.dto.response.TeamMemberListResponse;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.mapper.TeamMapper;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final TeamMapper teamMapper;
    private final RedisTeamService redisService;

    public InvitationCodeResponse makeInvitationCode(
            Long teamId,
            InvitationCodeRequest request,
            Long memberId
    ) {
        TeamMemberRole role = request.role();

        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.warn("Cannot make invitation: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        validateInvitationPermission(participant, role, teamId);
        String invitationUrl = redisService.generateInvitationUrl(teamId, role);

        return teamMapper.toInvitationCodeResponse(invitationUrl);
    }

    private void validateInvitationPermission(Participant participant, TeamMemberRole requestedRole, Long teamId) {
        TeamMemberRole currentRole = participant.getTeamMemberRole();

        if (currentRole == TeamMemberRole.VIEWER) {
            log.warn("VIEWER cannot invite: Participant ID = {}, Team ID = {}", participant.getId(), teamId);
            throw new CustomException(TeamErrorCode.VIEWER_CANNOT_INVITE);
        }

        if (requestedRole == TeamMemberRole.ADMIN) {
            if (currentRole != TeamMemberRole.ADMIN) {
                participantRepository.findByTeamIdAndTeamMemberRole(teamId, TeamMemberRole.ADMIN)
                        .ifPresent(admin -> {
                            log.warn("ADMIN already exists: Team ID = {}", teamId);
                            throw new CustomException(TeamErrorCode.ADMIN_EXISTS);
                        });
            }
        }
    }

    public GetTeamResponse findByTeamId(Long teamId, Long memberId) {
        //팀 id를 가지고 팀에 대한 정보 가져오기
        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.warn("Cannot get team: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        redisService.setMemberCurrentTeam(memberId, teamId, participant.getTeamMemberRole()
                .name());

        return teamMapper.toGetTeamResponse(team);
    }

    public TeamMemberListResponse findTeamMembers(Long teamId) {
        List<QueryTeamMember> members = teamRepository.findTeamMembers(teamId);

        //members 가 null인 경우, emptyList를 반환하도록 설정
        return teamMapper.toTeamMemberListResponse(
                Objects.requireNonNullElse(members, Collections.emptyList())
        );
    }


    public boolean existsByTeamId(Long teamId) {
        return teamRepository.existsById(teamId);
    }

    public TeamMemberListResponse searchParticipants(Long teamId, String name) {
        List<QueryTeamMember> mentions = participantRepository.findMentionsByNamePrefix(name, teamId);

        //mentions가 null인 경우, emptyList를 반환하도록 설정
        return teamMapper.toTeamMemberListResponse(
                Objects.requireNonNullElse(mentions, Collections.emptyList())
        );
    }
}
