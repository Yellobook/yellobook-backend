package com.yellobook.core.domain.team;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamRoleManager {
    private final TeamCachedRepository teamCachedRepository;

    @Autowired
    public TeamRoleManager(TeamCachedRepository teamCachedRepository) {
        this.teamCachedRepository = teamCachedRepository;
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
