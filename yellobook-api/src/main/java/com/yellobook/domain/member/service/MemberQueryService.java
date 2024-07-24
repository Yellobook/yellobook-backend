package com.yellobook.domain.member.service;

import com.yellobook.domains.member.entity.Member;

import java.util.Optional;

public interface MemberQueryService {
    Optional<Member> findById(Long memberId);
    Boolean getAllowanceById(Long memberId);
}
