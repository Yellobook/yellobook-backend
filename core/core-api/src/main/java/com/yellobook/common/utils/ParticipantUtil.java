package com.yellobook.common.utils;

import com.yellobook.support.error.code.AuthErrorCode;
import com.yellobook.support.error.code.TeamErrorCode;
import com.yellobook.support.error.exception.CustomException;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.TeamMemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipantUtil {
    private final ParticipantRepository participantRepository;

    /**
     * 관리자는 접근 불가능
     */
    public static void forbidAdmin(TeamMemberRole role) {
        if (role == TeamMemberRole.ADMIN) {
            throw new CustomException(AuthErrorCode.ADMIN_NOT_ALLOWED);
        }
    }

    /**
     * 주문자는 접근 불가능
     */
    public static void forbidOrderer(TeamMemberRole role) {
        if (role == TeamMemberRole.ORDERER) {
            throw new CustomException(AuthErrorCode.ORDERER_NOT_ALLOWED);
        }
    }

    /**
     * 뷰어는 접근 불가능
     */
    public static void forbidViewer(TeamMemberRole role) {
        if (role == TeamMemberRole.VIEWER) {
            throw new CustomException(AuthErrorCode.VIEWER_NOT_ALLOWED);
        }
    }

    /**
     * 권한 가져오기 + 팀에 해당하지 않는 멤버는 에러 반환
     */
    public TeamMemberRole getMemberTeamRole(Long teamId, Long memberId) {
        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(()
                        -> new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM));
        return participant.getTeamMemberRole();
    }

    /**
     * 팀에 관리자가 있는지 확인
     */
//    public Optional<Participant> findAdminByTeamIdAndRole(Long teamId){
//        return participantRepository.findByTeamIdAndRole(teamId, TeamMemberRole.ADMIN);
//    }
}
