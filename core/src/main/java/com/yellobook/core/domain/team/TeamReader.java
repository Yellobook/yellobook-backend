package com.yellobook.core.domain.team;


import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.yellobook.core.error.CoreErrorType.*;

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
     *
     * @param teamId 참여여부를 확인할 팀id
     * @param member 참여여부를 확인할 멤버 도메인 모델
     */
    public void validateParticipant(Long teamId, Member member) {
        if (!teamRepository.existByTeamIdAndMemberId(teamId, member.memberId())) {
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
                    throw new CoreException(ADMIN_EXISTS);
                }
            }
            case VIEWER -> {
                throw new CoreException(VIEWER_CANNOT_INVITE);
            }
            default -> {
                if (role == TeamMemberRole.ADMIN && teamRoleVerifier.hasAdmin(teamId)) {
                    throw new CoreException(ADMIN_EXISTS);
                }
            }
        }
    }

    /**
     * 키워드가 팀의 이름에 포함되어 있는 팀 리스트 반환
     * 팀은 공개팀만 조회 가능하다.
     *
     * @param keyword 키워드
     * @return 팀 리스트
     */
    public List<Team> readPublicTeamsByName(String keyword) {
        return teamRepository.getPublicTeamsByName(keyword.trim());
    }

    /**
     * 팀 가입 요청 정보 조회
     *
     * @param applyId 가입 요청 Id
     * @return 팀 가입 요청 정보
     */
    public TeamApplyInfo readTeamApplyInfo(Long applyId) {
        return teamRepository.findTeamApplyById(applyId).orElseThrow(() -> new CoreException(APPLY_TEAM_NOT_FOUND));
    }

    /**
     * 권한 변경 요청 정보 조회
     *
     * @param conversionId 권한 변경 요청 Id
     * @return 권한 변경 요청 정보
     */
    public RoleConversionInfo readRoleConversionInfo(Long conversionId) {
        return teamRepository.findTeamRoleConversionById(conversionId).orElseThrow(() -> new CoreException(ROLE_CONVERSION_NOT_FOUND));
    }
}
