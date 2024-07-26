package com.yellobook.domain.team.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domain.team.mapper.ParticipantMapper;
import com.yellobook.domain.team.mapper.TeamMapper;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TeamCommandServiceImpl implements TeamCommandService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final TeamMapper teamMapper;
    private final ParticipantMapper participantMapper;

    @Override
    public TeamResponse.CreateTeamResponseDTO createTeam(TeamRequest.CreateTeamRequestDTO request, CustomOAuth2User customOAuth2User){

        Member member = memberRepository.findById(customOAuth2User.getMemberId())
                .orElseThrow(() -> {
                    log.error("Member not found: {}", customOAuth2User.getMemberId());
                    return new CustomException(TeamErrorCode.MEMBER_NOT_FOUND);
                });

        Team newTeam = teamMapper.INSTANCE.toTeam(request);
        teamRepository.save(newTeam);
        log.info("New team created: {}", newTeam.getId());

        Participant founder = participantMapper.INSTANCE.toParticipant(request.getRole(),newTeam, member);
        participantRepository.save(founder);
        log.info("Participant added: Member ID = {}, Team ID = {}", member.getId(), newTeam.getId());

        return teamMapper.toCreateTeamResponseDTO(newTeam);
    }

    @Override
    public String inviteTeam(TeamRequest.InviteTeamRequestDTO request){
        return null;
    }

    @Override
    public TeamResponse.LeaveTeamResponseDTO leaveTeam(Long teamId, CustomOAuth2User customOAuth2User) {
        Long memberId = customOAuth2User.getMemberId();

        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.error("Participant not found for Member ID = {} and Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        participantRepository.delete(participant);
        log.info("Participant (Member ID = {}, Team ID = {}) is deleted", memberId, teamId);

        return teamMapper.toLeaveTeamResponseDTO(teamId);
    }

    @Override
    public TeamResponse.JoinTeamResponseDTO joinTeam(Long teamId, TeamRequest.JoinTeamRequestDTO request) {
        return null;
    }

}
