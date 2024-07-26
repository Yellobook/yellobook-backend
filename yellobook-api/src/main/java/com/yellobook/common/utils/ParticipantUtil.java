package com.yellobook.common.utils;

import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.enums.MemberTeamRole;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipantUtil {
    private final ParticipantRepository participantRepository;

    /**
     * 권한 가져오기 + 팀에 해당하지 않는 멤버는 에러 반환
     */
    public MemberTeamRole getMemberTeamRole(Long teamId, Long memberId){
        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId).orElseThrow(()
        -> new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM));
        return participant.getRole();
    }

    /**
     * 관리자는 접근 불가능
     */
    public void forbidAdmin(MemberTeamRole role){
        if(role == MemberTeamRole.ADMIN){
            throw new CustomException(AuthErrorCode.ADMIN_NOT_ALLOWED);
        }
    }

    /**
     * 주문자는 접근 불가능
     */
    public void forbidOrderer(MemberTeamRole role){
        if(role == MemberTeamRole.ORDERER){
            throw new CustomException(AuthErrorCode.ORDERER_NOT_ALLOWED);
        }
    }

    /**
     * 뷰어는 접근 불가능
     */
    public void forbidViewer(MemberTeamRole role){
        if(role == MemberTeamRole.VIEWER){
            throw new CustomException(AuthErrorCode.VIEWER_NOT_ALLOWED);
        }
    }

    /**
     * 팀에 관리자가 있는지 확인
     */
    public Optional<Participant> findAdminByTeamIdAndRole(Long teamId){
        return participantRepository.findByTeamIdAndRole(teamId, MemberTeamRole.ADMIN);
    }
}
