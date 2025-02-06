package com.yellobook.core.domain.member;

import org.springframework.stereotype.Component;

@Component
public class MemberWriter {
    public MemberWriter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    private final MemberRepository memberRepository;

    public Long add(
            ProfileInfo profileInfo,
            SocialInfo socialInfo
    ) {
        return memberRepository.save(new NewMember(profileInfo, socialInfo));
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }


}
