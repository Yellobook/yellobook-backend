package com.yellobook.domains.member.service;

import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.dto.response.ProfileResponse.ParticipantInfo;
import com.yellobook.domains.member.dto.response.TermAllowanceResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.mapper.MemberMapper;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.error.code.MemberErrorCode;
import com.yellobook.error.exception.CustomException;
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

}
