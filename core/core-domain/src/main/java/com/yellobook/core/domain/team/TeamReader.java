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
    public void isPresent(String name) {
        if (teamRepository.existByName(name)) {
            throw new CoreException(EXIST_TEAM_NAME);
        }
    }

    /*
    멤버가 속한 팀 목록을 가져오기
     */
    public List<Team> readTeamsByMemberId(Long memberId) {
        return teamRepository.getTeamsByMemberId(memberId);
    }


    /*
    팀 id를 통해 팀 가져오기
     */
    public Team read(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new CoreException(TEAM_NOT_FOUND));
    }


    /**
     * 키워드가 팀의 이름에 포함되어 있는 팀 리스트 반환 팀은 공개팀만 조회 가능하다.
     *
     * @param keyword 키워드
     * @return 팀 리스트
     */
    public List<Team> readSearchableTeamsByName(String keyword) {
        return teamRepository.getSearchableTeamsByName(keyword.trim());
    }

}
