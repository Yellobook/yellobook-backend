package com.yellobook.core.domain.member;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MemberReader {
    private final MemberRepository memberRepository;

    public MemberReader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member read(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CoreException(CoreErrorType.MEMBER_NOT_FOUND));
    }

    public Optional<Member> read(SocialInfo socialInfo) {
        return memberRepository.findBySocialInfo(socialInfo);
    }
}
