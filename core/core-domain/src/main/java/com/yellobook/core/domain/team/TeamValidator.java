package com.yellobook.core.domain.team;

import com.yellobook.core.enums.TeamMemberRole;
import com.yellobook.core.support.error.CoreErrorType;
import com.yellobook.core.support.error.CoreException;
import org.springframework.stereotype.Service;

@Service
public class TeamValidator {
    private final TeamRepository teamRepository;

    public TeamValidator(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * 관리자가 아니면 팀 생성 불가능
     *
     * @param role 역할
     */
    public void canCreateTeam(TeamMemberRole role) {
        if (!role.equals(TeamMemberRole.SELLER)) {
            throw new CoreException(CoreErrorType.STORE_CREATION_FAILED);
        }
    }

    /**
     * 관리가가 아니면 팀 검색 여부 변경 불가능
     *
     * @param role
     */
    public void canModifySearchable(TeamMemberRole role) {
        if (!role.equals(TeamMemberRole.SELLER)) {
            throw new CoreException(CoreErrorType.ONLY_SELLER_CAN_UPDATE);
        }
    }

    public void isMemberOfTeam(Long teamId, Long memberId) {
        if (!teamRepository.isTeamMember(teamId, memberId)) {
            throw new CoreException(CoreErrorType.USER_NOT_IN_THE_STORE);
        }
    }

    public void canJoinTeam(Long teamId, Long memberId) {
        if (teamRepository.isTeamMember(teamId, memberId)) {
            throw new CoreException(CoreErrorType.MEMBER_ALREADY_EXIST);
        }
    }

    /**
     * 팀의 가입 요청 수정 가능한지 검증 관리자, 주문자만 승인 또는 거절 할 수 있다.
     *
     * @param role 승인 또는 거절하는 사람의 역할
     */
    public void canUpdateTeamJoinRequest(TeamMemberRole role) {
        if (!(role.equals(TeamMemberRole.SELLER) || role.equals(TeamMemberRole.ORDERER))) {
            throw new CoreException(CoreErrorType.SELLER_AND_ORDERER_CAN_UPDATE_JOIN_REQUEST);
        }
    }

    /**
     * 주문자로 전환 요청 보낼 수 있는지 확인
     */
    public void canRequestOrdererConversion(Long teamId, Long memberId, TeamMemberRole role) {
        // 뷰어 인지 확인
        if (!role.equals(TeamMemberRole.VIEWER)) {
            throw new CoreException(CoreErrorType.ONLY_VIEWER_CAN_REQUESTED_ORDERER_CONVERSION);
        }
    }

    /**
     * 주문자로 역할 변경 요청을 수락 또는 거절 할 수 있는지 검증 관리자가 아니면 수락 또는 거절 불가능
     *
     * @param role 역할
     */
    public void canChangeTeamRole(TeamMemberRole role) {
        if (!role.equals(TeamMemberRole.SELLER)) {
            throw new CoreException(CoreErrorType.ONLY_SELLER_CAN_UPDATE);
        }
    }

    public void canCreateInvitationCode(TeamMemberRole role) {
        if (!role.equals(TeamMemberRole.SELLER)) {
            throw new CoreException(CoreErrorType.ONLY_SELLER_CAN_MAKE_CODE);
        }
    }

    public void canInviteWithRole(TeamMemberRole role) {
        if (role.equals(TeamMemberRole.ORDERER)) {
            throw new CoreException(CoreErrorType.CAN_INVITE_ORDERER);
        }
    }
}
