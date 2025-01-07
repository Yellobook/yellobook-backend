package com.yellobook.core.domain.team;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.MemberRole;

public record Participant(
        String participantId,
        Member member,
        Team team,
        MemberRole memberRole
) {
}
