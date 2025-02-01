//package com.yellobook.storage.db.core.team;
//
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.yellobook.domains.member.entity.QMember;
//import com.yellobook.storage.db.core.team.dto.query.QueryTeamMember;
//import com.yellobook.domains.team.entity.QParticipant;
//import com.yellobook.domains.team.entity.QTeam;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class TeamCustomRepositoryImpl implements TeamCustomRepository {
//    private final JPAQueryFactory queryFactory;
//
//    @Override
//    public List<QueryTeamMember> findTeamMembers(Long teamId) {
//        QMember member = QMember.member;
//        QParticipant participant = QParticipant.participant;
//        QTeam team = QTeam.team;
//        return queryFactory
//                .select(
//                        Projections.constructor(QueryTeamMember.class,
//                                member.id.as("memberId"),
//                                member.nickname.as("nickname")
//                        )
//                )
//                .from(participant)
//                .join(participant.team, team)
//                .join(participant.member, member)
//                .where(team.id.eq(teamId))
//                .fetch();
//    }
//}
