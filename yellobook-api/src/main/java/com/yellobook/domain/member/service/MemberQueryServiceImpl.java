package com.yellobook.domain.member.service;

import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);

    }

    @Override
    public Boolean getAllowanceById(Long memberId) {
        return memberRepository.findById(memberId).map(Member::getAllowance).orElse(false);
    }
}
