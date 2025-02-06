package com.yellobook.storage.db.core.member;


import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.member.JoinedTeamResult;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.MemberRepository;
import com.yellobook.core.domain.member.NewMember;
import com.yellobook.core.domain.member.SocialInfo;
import com.yellobook.core.enums.TeamMemberRole;
import com.yellobook.storage.db.core.team.QParticipantEntity;
import com.yellobook.storage.db.core.team.QTeamEntity;
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
                socialInfo.email()
        );
        return memberJpaRepository.save(member)
                .getId();
    }


    @Override
    public boolean existByEmail(String email) {
        QMemberEntity member = QMemberEntity.memberEntity;
        return queryFactory
                .selectOne()
                .from(member)
                .where(member.email.eq(email))
                .fetchFirst() != null;
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
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
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
    public List<JoinedTeamResult> findJoinedTeamsByMemberId(Long memberId) {
        QMemberEntity member = QMemberEntity.memberEntity;
        QParticipantEntity participant = QParticipantEntity.participantEntity;
        QTeamEntity team = QTeamEntity.teamEntity;
        QMemberEntity seller = new QMemberEntity("seller");
        QParticipantEntity sellerParticipant = new QParticipantEntity("sellerParticipant");
        return queryFactory.select(
                        new QJoinedTeamDto(
                                team.id,
                                team.name,
                                team.description,
                                participant.teamMemberRole.stringValue(),
                                seller.nickname,
                                JPAExpressions.select(participant.count()
                                                .castToNum(Integer.class))
                                        .from(participant)
                                        .where(participant.team.eq(team))
                        ))
                .from(participant)
                .join(participant.member, member)
                .join(participant.team, team)
                .join(sellerParticipant)
                .on(
                        sellerParticipant.team.eq(team),
                        sellerParticipant.teamMemberRole.eq(TeamMemberRole.SELLER)
                )
                .join(sellerParticipant.member, seller)
                .where(participant.member.id.eq(memberId))
                .groupBy(team.id, seller.nickname)
                .fetch()
                .stream()
                .map(JoinedTeamDto::toJoinedTeamResult)
                .toList();
    }
}
