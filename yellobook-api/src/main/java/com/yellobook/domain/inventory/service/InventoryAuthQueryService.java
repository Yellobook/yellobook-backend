package com.yellobook.domain.inventory.service;

import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.enums.MemberTeamRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryAuthQueryService {
    private final ParticipantRepository participantRepository;

    // 팀의 관리자 인지 확인
    public boolean isAdmin(Long teamId, Long memberId){
        Participant participant = getParticipant(teamId, memberId);
        return participant.getRole() == MemberTeamRole.ADMIN;
    }

    // 팀의 주문자 인지 확인
    public boolean isOrderer(Long teamId, Long memberId){
        Participant participant = getParticipant(teamId, memberId);
        return participant.getRole() == MemberTeamRole.ORDERER;
    }

    // 팀의 뷰어 인지 확인
    public boolean isViewer(Long teamId, Long memberId){
        Participant participant = getParticipant(teamId, memberId);
        return participant.getRole() == MemberTeamRole.VIEWER;
    }

    // participant 객체 가져오기
    private Participant getParticipant(Long teamId, Long memberId){
        Optional<Participant> optionalParticipant = participantRepository.findByTeamIdAndMemberId(teamId, memberId);
        if(optionalParticipant.isEmpty()){
            //TODO throw new CustomException();
        }
        return optionalParticipant.get();
    }

}
