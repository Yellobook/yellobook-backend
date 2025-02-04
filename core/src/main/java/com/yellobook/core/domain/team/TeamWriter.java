package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class TeamWriter {
    private final TeamRepository teamRepository;
    private final TeamApplyManager teamApplyManager;
    private final TeamRoleVerifier teamRoleVerifier;

    public TeamWriter(TeamRepository teamRepository,
                      TeamApplyManager invitationManager,
                      TeamRoleVerifier teamRoleVerifier) {
        this.teamRepository = teamRepository;
        this.teamApplyManager = invitationManager;
        this.teamRoleVerifier = teamRoleVerifier;
    }

    /*
    팀 생성
     */
    public Long create(String name, String phoneNumber, String address, Searchable searchable) {
        return teamRepository.save(name, phoneNumber, address, searchable);
    }

    /*
    팀 참가
     */
    public void join(Long teamId, Long memberId, TeamMemberRole role) {
        if (role == TeamMemberRole.ADMIN && teamRoleVerifier.hasAdmin(teamId)) {
            throw new CoreException(CoreErrorType.ADMIN_EXISTS);
        }
        teamRepository.join(teamId, memberId, role);
    }

    /*
    팀 나가기
     */
    public void leaveTeam(Long teamId, Long memberId, TeamMemberRole role) {
        teamRepository.leave(teamId, memberId);
        if (role.equals(TeamMemberRole.ADMIN)) {
            teamRepository.deactivateTeam(teamId);
        }
    }

    /*
    팀 초대 코드 발행
     */
    public String createInvitationCode(Long teamId) {
        return teamApplyManager.generateInvitationCode(teamId);
    }

    /**
     * 팀 공개 여부 수정
     */
    public void updateSearchable(Long teamId, Searchable searchable) {
        teamRepository.updateSearchable(teamId, searchable);
    }

    public void updateRole(Long teamId, Long memberId, TeamMemberRole role) {
        teamRepository.updateTeamMemberRole(teamId, memberId, role);
    }
}
