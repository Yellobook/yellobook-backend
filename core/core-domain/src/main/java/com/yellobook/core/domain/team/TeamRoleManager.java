package com.yellobook.core.domain.team;

import com.yellobook.core.enums.TeamMemberRole;
import com.yellobook.core.support.error.CoreErrorType;
import com.yellobook.core.support.error.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamRoleManager {
    private final TeamCachedRepository teamCachedRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public TeamRoleManager(TeamCachedRepository teamCachedRepository, TeamRepository teamRepository) {
        this.teamCachedRepository = teamCachedRepository;
        this.teamRepository = teamRepository;
    }

    public TeamMemberRole readRole(Long teamId, Long memberId) {
        Participant participant = teamRepository.findParticipantByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> new CoreException(CoreErrorType.USER_NOT_IN_THE_STORE));
        return participant.role();
    }

    public void updateRole(Long teamId, Long memberId, TeamMemberRole role) {
        teamRepository.updateTeamMemberRole(teamId, memberId, role);
    }

    public void requestOrdererConversion(Long teamId, Long memberId) {
        String key = generateOrdererConversionKey(teamId);
        teamCachedRepository.requestOrdererConversion(key, memberId);
    }

    public void deleteOrdererConversionRequest(Long teamId, Long memberId) {
        String key = generateOrdererConversionKey(teamId);
        if (!teamCachedRepository.isOrdererConversionRequestExist(key, memberId)) {
            throw new CoreException(CoreErrorType.ROLE_CONVERSION_NOT_FOUND);
        }
        teamCachedRepository.removeOrdererConversionRequest(key, memberId);
    }

    private String generateOrdererConversionKey(Long teamId) {
        return "team:role:toOrderer" + teamId;
    }


}
