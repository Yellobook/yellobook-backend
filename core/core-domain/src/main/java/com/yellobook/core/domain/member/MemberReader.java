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

    public boolean exist(String email) {
        return memberRepository.existByEmail(email);
    }

    public Member read(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CoreException(CoreErrorType.MEMBER_NOT_FOUND));
    }

    public Member read(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CoreException(CoreErrorType.MEMBER_NOT_FOUND));
    }

    public Optional<Member> find(String email) {
        return memberRepository.findByEmail(email);
    }
}
