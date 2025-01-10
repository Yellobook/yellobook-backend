package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamRoleVerifier {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamRoleVerifier(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    // 팀에 관리자가 있는지 검증
    public boolean hasAdmin(Long teamId) {
        return teamRepository.existByTeamAndRole(teamId, TeamMemberRole.ADMIN);
    }

}
