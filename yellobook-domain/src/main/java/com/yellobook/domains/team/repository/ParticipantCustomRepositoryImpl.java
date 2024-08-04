package com.yellobook.domains.team.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.member.entity.QMember;
import com.yellobook.domains.team.dto.MemberJoinTeamDTO;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.QParticipant;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParticipantCustomRepositoryImpl implements ParticipantCustomRepository{
    private final JPAQueryFactory queryFactory;

    public ParticipantCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<MemberJoinTeamDTO> getMemberJoinTeam(Long memberId) {
        QParticipant participant = QParticipant.participant;
        return queryFactory.select(Projections.constructor(MemberJoinTeamDTO.class,
                participant.role,
                participant.team.name.as("teamName")
                        ))
                .from(participant)
                .where(participant.member.id.eq(memberId))
                .orderBy(participant.team.name.asc())
                .fetch();
    }

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
