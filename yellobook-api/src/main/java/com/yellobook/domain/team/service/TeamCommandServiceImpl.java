package com.yellobook.domain.team.service;

import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamCommandServiceImpl implements TeamCommandService {

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(TeamRequest.CreateTeamRequestDTO request){
        return null;
    }

    @Override
    public String inviteTeam(TeamRequest.InviteTeamRequestDTO request){
        return null;
    }

}
