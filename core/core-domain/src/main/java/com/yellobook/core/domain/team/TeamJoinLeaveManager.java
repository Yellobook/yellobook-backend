package com.yellobook.core.domain.team;

import com.yellobook.core.domain.team.dto.InvitationInfo;
import com.yellobook.core.enums.TeamMemberRole;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class TeamJoinLeaveManager {
    private final TeamRepository teamRepository;
    private final TeamCachedRepository teamCachedRepository;
    private static final long validMinute = 15;

    public TeamJoinLeaveManager(TeamRepository teamRepository,
                                TeamCachedRepository teamCachedRepository) {
        this.teamRepository = teamRepository;
        this.teamCachedRepository = teamCachedRepository;
    }

    public void join(Long teamId, Long memberId, TeamMemberRole role) {
        teamRepository.join(teamId, memberId, role);
    }

    public void leaveTeam(Long teamId, Long memberId, TeamMemberRole role) {
        if (role.equals(TeamMemberRole.SELLER) && teamRepository.countAllByTeamIdAndTeamMemberRole(teamId, role) < 2) {
            throw new CoreException(CoreErrorType.SELLER_MUST_EXIST_IN_TEAM);
        }
        teamRepository.leave(teamId, memberId);
    }

    public String generateInvitationCode(Long teamId, TeamMemberRole inviteRole) {
        String code = InvitationCodeGenerator.generateNanoId();
        String key = generateInvitationKey(code);
        // 15분 간 유효한 링크 설정
        teamCachedRepository.saveInvitationCode(key, teamId, inviteRole, validMinute);
        return code;
    }

    public InvitationInfo getTeamIdByInvitationCode(String code) {
        InvitationInfo value = teamCachedRepository.readTeamIdAndRoleByCode("team:invite:" + code);
        if (value == null) {
            throw new CoreException(CoreErrorType.INVITATION_NOT_FOUND);
        }
        return value;
    }

    private String generateInvitationKey(String code) {
        return "team:invite:" + code;
    }

    public void requestTeamJoin(Long teamId, Long memberId) {
        String key = generateApplyTeamKey(teamId);
        teamCachedRepository.applyTeam(key, memberId);
    }

    public void deleteJoinRequest(Long teamId, Long memberId) {
        String key = generateApplyTeamKey(teamId);
        if (teamCachedRepository.isTeamJoinRequestExist(key, memberId)) {
            throw new CoreException(CoreErrorType.APPLY_TEAM_NOT_FOUND);
        }
        teamCachedRepository.removeTeamJoinRequest(key, memberId);
    }

    private String generateApplyTeamKey(Long teamId) {
        return "team:apply:" + teamId;
    }


}
