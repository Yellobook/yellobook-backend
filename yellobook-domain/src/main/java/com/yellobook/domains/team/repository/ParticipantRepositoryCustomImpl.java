package com.yellobook.domains.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.member.entity.QMember;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.QParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParticipantRepositoryCustomImpl implements ParticipantRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Participant> findMentionsByNamePrefix(String prefix, Long teamId){
        QMember member = QMember.member;
        QParticipant participant = QParticipant.participant;

        return queryFactory.selectFrom(participant)
                .join(participant.member, member)
                .where(member.nickname.like(prefix+ "%")
                        .and(participant.team.id.eq(teamId)))
                .fetch();
    }
}
