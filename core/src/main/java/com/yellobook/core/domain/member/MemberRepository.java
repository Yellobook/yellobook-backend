package com.yellobook.core.domain.member;

import java.util.Optional;

public interface MemberRepository {
    Long add(String profileImage, String email, String nickname, MemberRole role, Boolean allowance);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

}
