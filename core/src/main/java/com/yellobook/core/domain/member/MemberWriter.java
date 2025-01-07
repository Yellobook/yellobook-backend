package com.yellobook.core.domain.member;

public class MemberWriter {
    private final MemberRepository memberRepository;

    public MemberWriter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long add(
            String nickname,
            String email,
            String profileImage,
            MemberRole role,
            Boolean allowance
    ) {
        return memberRepository.add(nickname, email, profileImage, role, allowance);
    }
}
