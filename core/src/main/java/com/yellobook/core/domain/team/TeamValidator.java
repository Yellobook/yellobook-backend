package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Service;

@Service
public class TeamValidator {
    private final TeamRepository teamRepository;

    public TeamValidator(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /*
    TODO: controller 단으로 넘길 것
     */
    public boolean isValidCreation(String name, String phoneNumber, String address) {
        return name != null && !name.isEmpty() && phoneNumber != null && !phoneNumber.isEmpty() && address != null
                && !address.isEmpty() && !teamRepository.existByName(name);
    }

    /**
     * 관리자가 아니면 팀 생성 불가능
     *
     * @param role 역할
     */
    public void canCreateTeam(TeamMemberRole role) {
        if (!role.equals(TeamMemberRole.ADMIN)) {
            throw new CoreException(CoreErrorType.TEAM_CREATION_FAILED);
        }
    }

    /**
     * 관리가가 아니면 팀 검색 여부 변경 불가능
     *
     * @param role
     */
    public void canModifySearchable(TeamMemberRole role) {
        if (!role.equals(TeamMemberRole.ADMIN)) {
            throw new CoreException(CoreErrorType.ONLY_ADMIN_CAN_UPDATE);
        }
    }


    /**
     * 팀에 가입 요청을 할 수 있는지 검증 이미 가입한 사람이면 팀 참가 요청 불가능
     *
     * @param teamId   가입하고 싶은 팀 Id
     * @param memberId 가입하는 사람의 Id
     */
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
        if (!(role.equals(TeamMemberRole.ADMIN) || role.equals(TeamMemberRole.ORDERER))) {
            throw new CoreException(CoreErrorType.ADMIN_AND_ORDERER_CAN_UPDATE_JOIN_REQUEST);
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
        if (!role.equals(TeamMemberRole.ADMIN)) {
            throw new CoreException(CoreErrorType.ONLY_ADMIN_CAN_UPDATE);
        }
    }

    public void isMemberOfTeam(Long teamId, Long memberId) {
        if (!teamRepository.existByTeamIdAndMemberId(teamId, memberId)) {
            throw new CoreException(CoreErrorType.TEAM_MEMBER_NOT_FOUND);
        }
    }

    public void canCreateInvitationCode(TeamMemberRole role) {
        if (!role.equals(TeamMemberRole.ADMIN)) {
            throw new CoreException(CoreErrorType.ONLY_ADMIN_CAN_MAKE_CODE);
        }
    }
}
