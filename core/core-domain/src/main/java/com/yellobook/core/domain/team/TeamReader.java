package com.yellobook.core.domain.team;

import static com.yellobook.core.error.CoreErrorType.EXIST_TEAM_NAME;
import static com.yellobook.core.error.CoreErrorType.TEAM_NOT_FOUND;

import com.yellobook.core.error.CoreException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TeamReader {
    private final TeamRepository teamRepository;

    public TeamReader(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /*
    팀 이름 중복 여부 파악
     */
    public void isTeamNameExist(String name) {
        if (teamRepository.existByName(name)) {
            throw new CoreException(EXIST_TEAM_NAME);
        }
    }

    public Team read(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new CoreException(TEAM_NOT_FOUND));
    }

    public List<Team> readSearchableTeamsByName(String keyword) {
        return teamRepository.getSearchableTeamsByName(keyword.trim());
    }

    public List<Participant> getParticipants(Long teamId) {
        return teamRepository.getParticipantsByTeamId(teamId);
    }

}
