package com.yellobook.domain.team.service;

import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;

    @Override
    public Team findByTeamId(Long teamId, Long memberId){
        return null;
    }
}
