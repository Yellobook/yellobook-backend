package com.yellobook.core.domain.team;

import com.yellobook.core.enums.TeamMemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamRoleVerifier {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamRoleVerifier(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public boolean hasAdmin(Long teamId) {
        return teamRepository.existByTeamAndRole(teamId, TeamMemberRole.SELLER);
    }
}
