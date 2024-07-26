package com.yellobook.domain.team.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.auth.service.RedisService;
import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domain.team.mapper.TeamMapper;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.enums.MemberTeamRole;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final TeamMapper teamMapper;
    private final RedisService redisService;


    @Override
    public TeamResponse.InvitationCodeResponseDTO makeInvitationCode(
            Long teamId,
            TeamRequest.InvitationCodeRequestDTO request,
            CustomOAuth2User customOAuth2User
    ) {
        Long memberId = customOAuth2User.getMemberId();
        MemberTeamRole role = request.getRole();

        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.error("Participant not found: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        validateInvitationPermission(participant, role, teamId);

        return teamMapper.toInvitationCodeResponseDTO(redisService.generateInvitationLink(teamId, role));
    }

    private void validateInvitationPermission(Participant participant, MemberTeamRole requestedRole, Long teamId) {
        MemberTeamRole currentRole = participant.getRole();

        if (currentRole == MemberTeamRole.VIEWER) {
            log.error("VIEWER cannot invite: Participant ID = {}, Team ID = {}", participant.getId(), teamId);
            throw new CustomException(TeamErrorCode.VIEWER_CANNOT_INVITE);
        }

        if (requestedRole == MemberTeamRole.ADMIN) {
            if (currentRole != MemberTeamRole.ADMIN) {
                participantRepository.findByTeamIdAndRole(teamId, MemberTeamRole.ADMIN).ifPresent(admin -> {
                    log.error("ADMIN already exists: Team ID = {}", teamId);
                    throw new CustomException(TeamErrorCode.ADMIN_EXISTS);
                });
            }
        }
    }

    @Override
    public Team findByTeamId(Long teamId, Long memberId){
        return null;
    }
}
