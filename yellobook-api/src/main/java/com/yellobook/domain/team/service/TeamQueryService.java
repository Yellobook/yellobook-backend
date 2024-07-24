package com.yellobook.domain.team.service;


import com.yellobook.domains.team.entity.Team;

public interface TeamQueryService {
    Team findByTeamId(Long teamId, Long memberId);
}
