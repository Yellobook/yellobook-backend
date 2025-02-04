package com.yellobook.core.domain.team;


import static com.yellobook.core.error.CoreErrorType.EXIST_TEAM_NAME;
import static com.yellobook.core.error.CoreErrorType.MEMBER_ALREADY_EXIST;
import static com.yellobook.core.error.CoreErrorType.MEMBER_NOT_FOUND;
import static com.yellobook.core.error.CoreErrorType.TEAM_NOT_FOUND;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
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
     * 멤버의 팀 내 권한 확인
     */
    public TeamMemberRole readRole(Long teamId, Member member) {
        return teamRepository.getRole(teamId, member.memberId());
    }

    /**
     * team에 참여해야 할 때 사용 ex.convert
     */
    public void validateParticipant(Long teamId, Long memberId) {
        if (!teamRepository.existByTeamIdAndMemberId(teamId, memberId)) {
            throw new CoreException(MEMBER_NOT_FOUND);
        }
    }

    /**
     * team에 참여하지 않아야할 때 사용 ex.join
     *
     * @param teamId 참여여부를 확인할 팀id
     * @param member 참여여부를 확인할 멤버 도메인 모델
     */
    public void validateNotParticipant(Long teamId, Member member) {
        if (teamRepository.existByTeamIdAndMemberId(teamId, member.memberId())) {
            throw new CoreException(MEMBER_ALREADY_EXIST);
        }
    }

    /**
     * 키워드가 팀의 이름에 포함되어 있는 팀 리스트 반환 팀은 공개팀만 조회 가능하다.
     *
     * @param keyword 키워드
     * @return 팀 리스트
     */
    public List<Team> readPublicTeamsByName(String keyword) {
        return teamRepository.getPublicTeamsByName(keyword.trim());
    }

    public List<Participant> getParticipants(Long teamId) {
        return teamRepository.getMembersByTeamId(teamId);
    }
}
