package com.yellobook.storage.db.core.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.member.JoinedTeamResult;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.MemberRepository;
import com.yellobook.core.domain.member.NewMember;
import com.yellobook.core.domain.member.SocialInfo;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCoreRepository implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory queryFactory;

    public MemberCoreRepository(MemberJpaRepository memberJpaRepository,
                                JPAQueryFactory queryFactory) {
        this.memberJpaRepository = memberJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public Long save(NewMember newMember) {
        var profileInfo = newMember.profileInfo();
        var socialInfo = newMember.socialInfo();

        MemberEntity member = new MemberEntity(
                profileInfo.nickname(),
                profileInfo.bio(),
                profileInfo.profileImage(),
                socialInfo.oauthId(),
                socialInfo.provider(),
                socialInfo.email(),
                AppMemberRole.ROLE_USER
        );
        return memberJpaRepository.save(member)
                .getId();
    }

    @Override
    public boolean existById(Long memberId) {
        return memberJpaRepository.existsById(memberId);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .map(MemberEntity::toMember);
    }

    @Override
    public Optional<Member> findBySocialInfo(SocialInfo socialInfo) {
        return memberJpaRepository.findByOauthIdAndOauthProvider(socialInfo.oauthId(), socialInfo.provider())
                .map(MemberEntity::toMember);
    }

    @Override
    @Transactional
    public void delete(Member member) {
        memberJpaRepository.deleteById(member.memberId());
    }

    @Override
    public LocalDateTime getNicknameUpdatedAt(Member member) {
        return memberJpaRepository.findNicknameUpdatedAt(member.memberId());
    }

    @Override
    @Transactional
    public void updateNickname(Member member, String newNickname) {
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(member.memberId());
        memberEntity.updateNickname(newNickname);
    }

    @Override
    @Transactional
    public void updateBio(Member member, String newBio) {
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(member.memberId());
        memberEntity.updateBio(newBio);
    }

    @Override
    public List<JoinedTeamResult> findJoinedTeamsByMemberId(Member member) {
        return null;
    }

}
