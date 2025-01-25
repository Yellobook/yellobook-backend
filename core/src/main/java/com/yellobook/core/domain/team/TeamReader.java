package com.yellobook.core.domain.team;


import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TeamReader {
    private final TeamRepository teamRepository;
    private final TeamRoleVerifier teamRoleVerifier;

    public TeamReader(TeamRepository teamRepository, TeamRoleVerifier teamRoleVerifier) {
        this.teamRepository = teamRepository;
        this.teamRoleVerifier = teamRoleVerifier;
    }

    /*
    팀 이름 중복 여부 파악
     */
    public void isPresent(String name) {
        if (teamRepository.existByName(name)) {
            throw new CoreException(CoreErrorType.EXIST_TEAM_NAME);
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
                .orElseThrow(() -> new CoreException(CoreErrorType.TEAM_NOT_FOUND));
    }

    /**
     * 멤버의 팀 내 권한 확인
     */
    public TeamMemberRole readRole(Long teamId, Member member) {
        return teamRepository.getRole(teamId, member.memberId());
    }

    /**
     * team에 참여해야 할 때 사용 ex.convert
     *
     * @param teamId 참여여부를 확인할 팀id
     * @param member 참여여부를 확인할 멤버 도메인 모델
     */
    public void validateParticipant(Long teamId, Member member) {
        if (!teamRepository.existByTeamAndMemberId(teamId, member)) {
            throw new CoreException(CoreErrorType.MEMBER_NOT_FOUND);
        }
    }

    /**
     * team에 참여하지 않아야할 때 사용 ex.join
     *
     * @param teamId 참여여부를 확인할 팀id
     * @param member 참여여부를 확인할 멤버 도메인 모델
     */
    public void validateNotParticipant(Long teamId, Member member) {
        if (teamRepository.existByTeamAndMemberId(teamId, member)) {
            throw new CoreException(CoreErrorType.MEMBER_ALREADY_EXIST);
        }
    }

    /**
     * @param teamId 초대하는 팀 id
     * @param role   초대 코드에 있는 권한
     * @param member 초대를 하는 멤버
     */
    public void invitationRoleValidator(Long teamId, TeamMemberRole role, Member member) {
        //초대하는 멤버의 권한
        TeamMemberRole memberRole = readRole(teamId, member);
        switch (memberRole) {
            case ADMIN -> {
                if (role == TeamMemberRole.ADMIN) {
                    throw new CoreException(CoreErrorType.ADMIN_EXISTS);
                }
            }
            case VIEWER -> {
                throw new CoreException(CoreErrorType.VIEWER_CANNOT_INVITE);
            }
            default -> {
                if (role == TeamMemberRole.ADMIN && teamRoleVerifier.hasAdmin(teamId)) {
                    throw new CoreException(CoreErrorType.ADMIN_EXISTS);
                }
            }
        }
    }
}
