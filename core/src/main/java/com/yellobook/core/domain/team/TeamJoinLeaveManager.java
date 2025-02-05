package com.yellobook.core.domain.team;

import static com.yellobook.core.error.CoreErrorType.MEMBER_ALREADY_EXIST;
import static com.yellobook.core.error.CoreErrorType.MEMBER_NOT_FOUND;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.dto.InvitationInfo;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TeamJoinLeaveManager {
    private final TeamRepository teamRepository;
    private final TeamCachedRepository teamCachedRepository;
    private final TeamRoleVerifier teamRoleVerifier;
    private static final long validMinute = 15;

    public TeamJoinLeaveManager(TeamRepository teamRepository,
                                TeamCachedRepository teamCachedRepository, TeamRoleVerifier teamRoleVerifier) {
        this.teamRepository = teamRepository;
        this.teamCachedRepository = teamCachedRepository;
        this.teamRoleVerifier = teamRoleVerifier;
    }


    /**
     * team에 참여해야 할 때 사용 ex.convert
     */
    public void hasJoinedTeam(Long teamId, Long memberId) {
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
    public void hasNotJoinedTeam(Long teamId, Member member) {
        if (teamRepository.existByTeamIdAndMemberId(teamId, member.memberId())) {
            throw new CoreException(MEMBER_ALREADY_EXIST);
        }
    }

    public List<Participant> getParticipants(Long teamId) {
        return teamRepository.getMembersByTeamId(teamId);
    }

    /*
    팀 참가
    */
    public void join(Long teamId, Long memberId, TeamMemberRole role) {
        if (role == TeamMemberRole.SELLER && teamRoleVerifier.hasAdmin(teamId)) {
            throw new CoreException(CoreErrorType.SELLER_EXISTS);
        }
        teamRepository.join(teamId, memberId, role);
    }

    /*
    팀 나가기
     */
    public void leaveTeam(Long teamId, Long memberId, TeamMemberRole role) {
        if (role.equals(TeamMemberRole.SELLER) && teamRepository.countAllByTeamIdAndTeamMemberRole(teamId, role) < 2) {
            throw new CoreException(CoreErrorType.SELLER_MUST_EXIST_IN_TEAM);
        }
        teamRepository.leave(teamId, memberId);
    }

    /*
    초대 url 생성
     */
    public String generateInvitationCode(Long teamId, TeamMemberRole inviteRole) {
        String code = InvitationCodeGenerator.generateNanoId();
        String key = generateInvitationKey(code);
        // 15분 간 유효한 링크 설정
        teamCachedRepository.saveInvitationCode(key, teamId, inviteRole, validMinute);
        return code;
    }

    /*
    초대 정보 확인
     */
    public InvitationInfo getTeamIdByInvitationCode(String code) {
        InvitationInfo value = teamCachedRepository.readTeamIdAndRoleByCode("team:invite:" + code);
        if (value == null) {
            throw new CoreException(CoreErrorType.INVITATION_NOT_FOUND);
        }
        return value;
    }

    /*
    초대 키 발행
     */
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
