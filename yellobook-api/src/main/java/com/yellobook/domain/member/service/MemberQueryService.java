package com.yellobook.domain.member.service;

import com.yellobook.domain.member.dto.MemberResponse;
import com.yellobook.domain.member.mapper.MemberMapper;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.error.code.MemberErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;
    private final MemberMapper memberMapper;

    public MemberResponse.ProfileResponseDTO getMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<MemberResponse.ParticipantInfo> participantInfos = participantRepository.getMemberJoinTeam(memberId).stream()
                .map(MemberResponse.ParticipantInfo::new).toList();
        return memberMapper.toProfileResponseDTO(member, participantInfos);
    }

    public MemberResponse.AllowanceResponseDTO getAllowanceById(Long memberId) {
        boolean allowed = memberRepository.findById(memberId).map(Member::getAllowance).orElse(false);
        return MemberResponse.AllowanceResponseDTO.builder().allowance(allowed).build();
    }

}
