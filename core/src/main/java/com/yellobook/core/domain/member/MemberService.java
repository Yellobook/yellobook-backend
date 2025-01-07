package com.yellobook.core.domain.member;

import com.yellobook.core.domain.team.Team;
import com.yellobook.core.domain.team.TeamReader;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
//@Transactional(readOnly = true)
public class MemberService {

    private final MemberWriter memberWriter;
    private final MemberReader memberReader;
    // 사용사가 속한 팀 조회 -> MemberReader 의 반환값이 List<Team> 이다
    private final TeamReader teamReader;

    public MemberService(MemberWriter memberWriter, MemberReader memberReader, TeamReader teamReader) {
        this.memberWriter = memberWriter;
        this.memberReader = memberReader;
        this.teamReader = teamReader;
    }

    public Long registerMember(String nickname, String email, String profileImage) {
        return memberWriter.add(nickname, email, profileImage, MemberRole.USER, false);
    }

    @Transactional
    public Member read(Long memberId) {
        return memberReader.read(memberId).orElseThrow(() -> new CustomException);
    }

    public Boolean getMemberAllowance(Long memberId) {
        return memberReader.read(memberId).allowance();
    }

    public List<Team> getMemberJoinedTeams(Long memberId) {
        return teamReader.readTeamsByMemberId(memberId);
    }

//    public ProfileResponse getMemberProfile(Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
//        List<ParticipantInfo> participantInfos = participantRepository.getMemberJoinTeam(memberId)
//                .stream()
//                .map(memberMapper::toParticipantInfo)
//                .toList();
//        return memberMapper.toProfileResponseDTO(member, participantInfos);
//    }



//    public TermAllowanceResponse getAllowanceById(Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
//        boolean allowed = member.getAllowance() != null ? member.getAllowance() : false;
//        return memberMapper.toAllowanceResponseDTO(allowed);
//    }


    public CurrentTeamResponse getMemberCurrentTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new CustomException(MemberErrorCode.MEMBER_TEAM_NOT_FOUND));
        return memberMapper.toCurrentTeamResponse(team);
    }
}
