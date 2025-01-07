package com.yellobook.core.domain.team;


import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TeamReader {
    private final TeamRepository teamRepository;

    public TeamReader(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> readTeamsByMemberId(Long memberId) {
        return teamRepository.getTeamsByMemberId(memberId);
    }
}
