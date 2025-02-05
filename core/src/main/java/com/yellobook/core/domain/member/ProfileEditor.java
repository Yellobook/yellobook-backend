package com.yellobook.core.domain.member;

import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ProfileEditor {
    private final MemberRepository memberRepository;

    public ProfileEditor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean canChangeNickname(Member member) {
        LocalDateTime lastUpdateTime = memberRepository.getNicknameUpdatedAt(member);
        return lastUpdateTime != null && Duration.between(lastUpdateTime, LocalDateTime.now())
                .toDays() >= 5;
    }

    public void updateNickname(Member member, String newNickname) {
        memberRepository.updateNickname(member, newNickname);
    }

    public void updateBio(Member member, String newBio) {
        memberRepository.updateBio(member, newBio);
    }
}
