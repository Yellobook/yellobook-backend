package com.yellobook.team;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.team.dto.query.QueryTeam;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TeamCustomRepositoryImpl implements TeamCustomRepository {
    private final JPAQueryFactory queryFactory;

    public TeamCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<QueryTeamMember> findTeamMembers(Long teamId) {
        QMember member = QMember.member;
        QParticipant participant = QParticipant.participant;
        QTeam team = QTeam.team;
        return queryFactory
                .select(
                        Projections.constructor(QueryTeamMember.class,
                                member.id.as("memberId"),
                                member.nickname.as("nickname")
                        )
                )
                .from(participant)
                .join(participant.team, team)
                .join(participant.member, member)
                .where(team.id.eq(teamId))
                .fetch();
    }

    @Override
    public List<QueryTeam> getTeamsByMemberId(Long memberId) {
        QTeamEntity team = QteamEntity.teamEntity;
        QParticipant participant = QParticipant.participant;

        List<TeamEntity> teamEntities = queryFactory
                .selectFrom(team)
                .join(participant)
                .on(participant.team.id.eq(team.id))
                .where(participant.member.id.eq(memberId))
                .fetch();

        return teamEntites.stream()
                .map(TeamEntity::toTeam)
                .toList();
    }
}
