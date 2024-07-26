package com.yellobook.domain.member.service;

import com.yellobook.domain.member.dto.MemberResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.error.code.MemberErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberResponse.ProfileResponseDTO getMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

    }

    public MemberResponse.AllowanceResponseDTO getAllowanceById(Long memberId) {
        boolean allowed = memberRepository.findById(memberId).map(Member::getAllowance).orElse(false);
        return MemberResponse.AllowanceResponseDTO.builder().allowance(allowed).build();
    }

}
