package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.dto.CreateInvitationCodeCommand;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class TeamWriter {
    private final TeamRepository teamRepository;
    private final TeamCachedManager teamRedisManager;
    private final TeamRoleVerifier teamRoleVerifier;

    public TeamWriter(TeamRepository teamRepository,
                      TeamReader teamReader,
                      TeamCachedManager invitationManager,
                      TeamRoleVerifier teamRoleVerifier) {
        this.teamRepository = teamRepository;
        this.teamRedisManager = invitationManager;
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
    public void join(Long teamId, Member member, TeamMemberRole role) {
        if (role == TeamMemberRole.ADMIN && teamRoleVerifier.hasAdmin(teamId)) {
            throw new CoreException(CoreErrorType.ADMIN_EXISTS);
        }
        teamRepository.join(teamId, member, role);
    }

    /*
    팀 나가기
     */
    public void leave(Long teamId, Member member) {
        teamRepository.leave(teamId, member);
    }

    /*
    팀 초대 코드 발행
     */
    public String createInvitationCode(CreateInvitationCodeCommand command) {
        return teamRedisManager.generateInvitationUrl(command.teamId(), command.role());
    }

    /**
     * 팀 공개 여부 수정
     */
    public void updateSearchable(Long teamId, Searchable searchable) {
        teamRepository.updateSearchable(teamId, searchable);
    }

    /**
     * 팀에 가입 요청하기
     *
     * @param teamId   가입하고 싶은 팀 Id
     * @param memberId 가입하는 사람 Id
     */
    public void requestJoinTeam(Long teamId, Long memberId) {
        teamRepository.applyTeam(teamId, memberId);
    }

    /**
     * 팀 가입 요청 승인
     *
     * @param applyInfo
     */
    public void acceptJoinRequest(TeamApplyInfo applyInfo) {
        // joinStatus 변경
        teamRepository.updateJoinStatus(applyInfo.applyId(), JoinStatus.ACCEPTED);
        // 팀에 추가
        requestJoinTeam(applyInfo.teamId(), applyInfo.memberId());
    }

    /**
     * 팀 가입 요청 거절
     *
     * @param teamApplyId
     */
    public void rejectJoinRequest(Long teamApplyId) {
        teamRepository.updateJoinStatus(teamApplyId, JoinStatus.REJECTED);
    }

    /**
     * 주문자로 변경 요청
     *
     * @param teamId   팀 Id
     * @param memberId 사용자 Id
     */
    public void requestOrdererConversion(Long teamId, Long memberId) {
        teamRepository.requestOrdererConversion(teamId, memberId);
    }

    /**
     * 주문자로 권한 변경 요청 수락
     *
     * @param info 권한 변경 요청 정보
     */
    public void acceptOrdererConversion(RoleConversionInfo info) {
        teamRepository.updateRoleConversionStatus(info.conversionId(), ChangeRoleStatus.ACCEPTED);
        teamRepository.updateTeamMemberRole(info.teamId(), info.memberId(), info.requestRole());
    }

    /**
     * 주문자로 변경 요청 수락
     *
     * @param requestId 권한 변경 요청 Id
     */
    public void rejectOrdererConversion(Long requestId) {
        teamRepository.updateRoleConversionStatus(requestId, ChangeRoleStatus.REJECTED);
    }
}
