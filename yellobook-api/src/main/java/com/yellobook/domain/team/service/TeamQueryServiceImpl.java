package com.yellobook.domain.team.service;

import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domain.inform.dto.MentionDTO;
import com.yellobook.domain.team.mapper.ParticipantMapper;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.auth.service.RedisTeamService;
import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domain.team.mapper.TeamMapper;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.ParticipantRepositoryCustom;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final TeamMapper teamMapper;
    private final RedisTeamService redisService;
    private final ParticipantMapper participantMapper;

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
                    log.error("Cannot make invitation: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        validateInvitationPermission(participant, role, teamId);
        String invitationUrl = redisService.generateInvitationUrl(teamId, role);

        return teamMapper.toInvitationCodeResponseDTO(invitationUrl);
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
    public TeamResponse.GetTeamResponseDTO findByTeamId(Long teamId, CustomOAuth2User customOAuth2User){
        //팀 id를 가지고 팀에 대한 정보 가져오기

        Long memberId = customOAuth2User.getMemberId();

        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() ->{
                    log.error("Cannot get team: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        Team team = teamRepository.getReferenceById(teamId);

        return teamMapper.toGetTeamResponseDTO(team);
    }

    public boolean existsByTeamId(Long teamId) {
        return teamRepository.existsById(teamId);
    }

    @Override
    public List<MentionDTO> searchParticipants(Long teamId, String name){
        List<Participant> mentions;

        if(name.equalsIgnoreCase("@everyone")){
            mentions = participantRepository.findAllByTeamId(teamId);
        }
        else if(name.startsWith("@")){
            String prefix = name.substring(1);
            mentions = participantRepository.findMentionsByNamePrefix(prefix, teamId);
        }
        else {
            return List.of();
        }
        return participantMapper.toMentionDTOlist(mentions);
    }
}
