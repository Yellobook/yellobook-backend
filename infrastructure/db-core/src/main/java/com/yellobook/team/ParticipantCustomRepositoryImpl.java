package com.yellobook.team;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.member.entity.QMember;
import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.entity.QParticipant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ParticipantCustomRepositoryImpl implements ParticipantCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<QueryMemberJoinTeam> getMemberJoinTeam(Long memberId) {
        QParticipant participant = QParticipant.participant;
        return queryFactory.select(Projections.constructor(QueryMemberJoinTeam.class,
                        participant.teamMemberRole,
                        participant.team.id.as("teamId"),
                        participant.team.name.as("teamName")
                ))
                .from(participant)
                .where(participant.member.id.eq(memberId))
                .orderBy(participant.team.name.asc())
                .fetch();
    }

    @Override
    public List<QueryTeamMember> findMentionsByNamePrefix(String prefix, Long teamId) {
        QMember member = QMember.member;
        QParticipant participant = QParticipant.participant;

        return queryFactory
                .select(Projections.constructor(QueryTeamMember.class,
                        member.id,
                        member.nickname
                ))
                .from(participant)
                .join(participant.member, member)
                .where(member.nickname.like(prefix + "%")
                        .and(participant.team.id.eq(teamId)))
                .fetch();
    }
}
