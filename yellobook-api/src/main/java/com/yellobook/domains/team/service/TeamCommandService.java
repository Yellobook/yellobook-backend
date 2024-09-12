package com.yellobook.domains.team.service;

import com.yellobook.domains.auth.dto.InvitationResponse;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.team.dto.request.*;
import com.yellobook.domains.team.dto.response.*;
import com.yellobook.domains.team.mapper.ParticipantMapper;
import com.yellobook.domains.team.mapper.TeamMapper;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.entity.Participant;
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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TeamCommandService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final TeamMapper teamMapper;
    private final ParticipantMapper participantMapper;
    private final RedisTeamService redisService;

    public CreateTeamResponse createTeam(CreateTeamRequest request, Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("Member {} not found.", memberId);
                    return new CustomException(TeamErrorCode.MEMBER_NOT_FOUND);
                });
        if(teamRepository.findByName(request.name()).isPresent()){
            log.warn("Team {} already exists.", request.name());
            throw new CustomException(TeamErrorCode.EXIST_TEAM_NAME);
        }
        else{
            Team newTeam = teamMapper.toTeam(request);
            teamRepository.save(newTeam);
            log.info("New team created: {}", newTeam.getId());
            Participant founder = participantMapper.toParticipant(request.role(),newTeam, member);
            participantRepository.save(founder);
            log.info("Participant added: Member ID = {}, Team ID = {}", member.getId(), newTeam.getId());

            redisService.setMemberCurrentTeam(member.getId(), newTeam.getId(), request.role().name());

            return teamMapper.toCreateTeamResponse(newTeam);
        }
    }

    public void leaveTeam(Long teamId, Long memberId) {
        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.warn("Participant not found for Member ID = {} and Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        participantRepository.delete(participant);
        log.info("Participant (Member ID = {}, Team ID = {}) is deleted", memberId, teamId);

        participantRepository.findFirstByMemberIdOrderByCreatedAtAsc(memberId)
                .ifPresent(tempParticipant -> {
                    redisService.setMemberCurrentTeam(
                            memberId,
                            tempParticipant.getTeam().getId(),
                            tempParticipant.getRole().name()
                    );
                });
    }

    public JoinTeamResponse joinTeam(Long memberId, String code) {

        InvitationResponse invitationData = redisService.getInvitationInfo(code);
        Long teamId = invitationData.getTeamId();
        MemberTeamRole role = invitationData.getRole();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("Member not found: {}", memberId);
                    return new CustomException(TeamErrorCode.MEMBER_NOT_FOUND);
                });

        //팀이 유효한가?
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        if (participantRepository.findByTeamIdAndMemberId(teamId, memberId).isPresent()){
            log.warn("Member {} already on the team {}", memberId, teamId);
            throw new CustomException(TeamErrorCode.MEMBER_ALREADY_EXIST);
        }
        // ADMIN이 있는가?
        if (role == MemberTeamRole.ADMIN) {
            participantRepository.findByTeamIdAndRole(teamId, MemberTeamRole.ADMIN).ifPresent(
                    admin -> {
                        log.warn("Admin already exists in team {}", teamId);
                        throw new CustomException(TeamErrorCode.ADMIN_EXISTS);
                    });
        }
        participantRepository.save(participantMapper.toParticipant(role, team, member));
        log.info("Participant added: Team ID = {}, Role = {}, Member ID = {}", teamId, role, memberId);

        redisService.setMemberCurrentTeam(memberId, teamId, role.name());

        return teamMapper.toJoinTeamResponse(team);
    }

}
