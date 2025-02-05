package com.yellobook.core.domain.member;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JoinedTeamReader {
    public JoinedTeamReader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    private final MemberRepository memberRepository;

    public List<JoinedTeamResult> read(Member member) {
        return memberRepository.findJoinedTeamsByMemberId(member);
    }
}
