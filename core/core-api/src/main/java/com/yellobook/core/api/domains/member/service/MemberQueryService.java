package com.yellobook.core.api.domains.member.service;

import com.yellobook.core.api.domains.member.dto.response.CurrentTeamResponse;
import com.yellobook.core.api.domains.member.dto.response.ProfileResponse;
import com.yellobook.core.api.domains.member.dto.response.ProfileResponse.ParticipantInfo;
import com.yellobook.core.api.domains.member.dto.response.TermAllowanceResponse;
import com.yellobook.core.domains.member.entity.Member;
import com.yellobook.core.api.domains.member.mapper.MemberMapper;
import com.yellobook.core.domains.member.repository.MemberRepository;
import com.yellobook.core.domains.team.entity.Team;
import com.yellobook.core.domains.team.repository.ParticipantRepository;
import com.yellobook.core.domains.team.repository.TeamRepository;
import com.yellobook.core.api.support.error.code.MemberErrorCode;
import com.yellobook.core.api.support.error.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;
    private final TeamRepository teamRepository;
    private final MemberMapper memberMapper;

    public ProfileResponse getMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<ParticipantInfo> participantInfos = participantRepository.getMemberJoinTeam(memberId)
                .stream()
                .map(memberMapper::toParticipantInfo)
                .toList();
        return memberMapper.toProfileResponseDTO(member, participantInfos);
    }

    public TermAllowanceResponse getAllowanceById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        boolean allowed = member.getAllowance() != null ? member.getAllowance() : false;
        return memberMapper.toAllowanceResponseDTO(allowed);
    }

    public CurrentTeamResponse getMemberCurrentTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new CustomException(MemberErrorCode.MEMBER_TEAM_NOT_FOUND));
        return memberMapper.toCurrentTeamResponse(team);
    }
}
