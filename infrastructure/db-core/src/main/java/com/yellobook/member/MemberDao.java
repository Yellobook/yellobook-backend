package com.yellobook.member;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.MemberRepository;
import com.yellobook.core.domain.member.MemberRole;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    public MemberDao(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Long add(String nickname, String email, String profileImage, MemberRole role, Boolean allowance) {
        MemberEntity member = MemberEntity.builder()
                .nickname(nickname)
                .email(email)
                .profileImage(profileImage)
                .role(role)
                .allowance(allowance)
                .build();
        return memberJpaRepository.save(member)
                .getId();
    }
    

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .map(MemberEntity::toMember);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .map(MemberEntity::toMember);

    }
}
