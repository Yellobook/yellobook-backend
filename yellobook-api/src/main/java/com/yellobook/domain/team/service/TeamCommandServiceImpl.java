package com.yellobook.domain.team.service;

import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamCommandServiceImpl implements TeamCommandService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;

    @Override
    public Team createTeam(TeamRequest.CreateTeamRequestDTO request){
        return null;
    }

    @Override
    public String inviteTeam(TeamRequest.InviteTeamRequestDTO request){
        return null;
    }

    @Override
    public Boolean leaveTeam(Long teamId, Long participantId) {
        return null;
    }

    @Override
    public Participant joinTeam(Long teamId, TeamRequest.JoinTeamRequestDTO request) {
        return null;
    }

}
