package com.yellobook.domains.team.service;

import com.yellobook.domains.inform.dto.MentionDTO;
import com.yellobook.domains.team.mapper.ParticipantMapper;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.team.dto.request.InvitationCodeRequest;
import com.yellobook.domains.team.dto.response.*;
import com.yellobook.domains.team.mapper.TeamMapper;
import com.yellobook.domains.team.entity.Team;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TeamQueryService{

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final TeamMapper teamMapper;
    private final RedisTeamService redisService;
    private final ParticipantMapper participantMapper;

    public InvitationCodeResponse makeInvitationCode(
            Long teamId,
            InvitationCodeRequest request,
            CustomOAuth2User customOAuth2User
    ) {
        Long memberId = customOAuth2User.getMemberId();
        MemberTeamRole role = request.role();

        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.warn("Cannot make invitation: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        validateInvitationPermission(participant, role, teamId);
        String invitationUrl = redisService.generateInvitationUrl(teamId, role);

        return teamMapper.toInvitationCodeResponse(invitationUrl);
    }

    private void validateInvitationPermission(Participant participant, MemberTeamRole requestedRole, Long teamId) {
        MemberTeamRole currentRole = participant.getRole();

        if (currentRole == MemberTeamRole.VIEWER) {
            log.warn("VIEWER cannot invite: Participant ID = {}, Team ID = {}", participant.getId(), teamId);
            throw new CustomException(TeamErrorCode.VIEWER_CANNOT_INVITE);
        }

        if (requestedRole == MemberTeamRole.ADMIN) {
            if (currentRole != MemberTeamRole.ADMIN) {
                participantRepository.findByTeamIdAndRole(teamId, MemberTeamRole.ADMIN).ifPresent(admin -> {
                    log.warn("ADMIN already exists: Team ID = {}", teamId);
                    throw new CustomException(TeamErrorCode.ADMIN_EXISTS);
                });
            }
        }
    }

    public GetTeamResponse findByTeamId(Long teamId, CustomOAuth2User customOAuth2User){
        //팀 id를 가지고 팀에 대한 정보 가져오기

        Long memberId = customOAuth2User.getMemberId();

        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() ->{
                    log.warn("Cannot get team: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        Team team = teamRepository.getReferenceById(teamId);

        redisService.setMemberCurrentTeam(memberId,teamId,participant.getRole().name());

        return teamMapper.toGetTeamResponse(team);
    }

    public boolean existsByTeamId(Long teamId) {
        return teamRepository.existsById(teamId);
    }

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
