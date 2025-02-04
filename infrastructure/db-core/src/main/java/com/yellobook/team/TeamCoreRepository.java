package com.yellobook.team;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.team.Participant;
import com.yellobook.core.domain.team.Searchable;
import com.yellobook.core.domain.team.Team;
import com.yellobook.core.domain.team.TeamRepository;
import com.yellobook.member.MemberEntity;
import com.yellobook.member.MemberJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TeamCoreRepository implements TeamRepository {
    private final TeamJpaRepository teamJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory queryFactory;

    public TeamCoreRepository(TeamJpaRepository teamJpaRepository, ParticipantJpaRepository participantJpaRepository,
                              MemberJpaRepository memberJpaRepository, JPAQueryFactory queryFactory) {
        this.teamJpaRepository = teamJpaRepository;
        this.participantJpaRepository = participantJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public boolean existByTeamAndMemberAndRole(Long teamId, Long memberId, TeamMemberRole role) {
        return participantJpaRepository.existsByTeamIdAndMemberIdAndTeamMemberRole(teamId, memberId, role);
    }

    @Override
    public boolean existByTeamAndRole(Long teamId, TeamMemberRole role) {
        return participantJpaRepository.existsByTeamIdAndTeamMemberRole(teamId, role);
    }

    @Override
    public List<Team> getTeamsByMemberId(Long memberId) {
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

    @Override
    public Long save(String name, String phoneNumber, String address, Searchable searchable) {
        TeamEntity team = TeamEntity.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .searchable(searchable)
                .build();
        return teamJpaRepository.save(team)
                .getId();
    }

    @Override
    public Optional<Team> findById(Long teamId) {
        return teamJpaRepository.findById(teamId)
                .map(TeamEntity::toTeam);
    }

    @Override
    public List<Participant> getMembersByTeamId(Long teamId) {
        return participantJpaRepository.findAllByTeamId(teamId)
                .stream()
                .map(ParticipantEntity::toParticipant)
                .toList();
    }

    @Override
    public void join(Long teamId, Long memberId, TeamMemberRole role) {
        TeamEntity teamEntity = teamJpaRepository.getReferenceById(teamId);
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(memberId);
        participantJpaRepository.save(new ParticipantEntity(teamEntity, memberEntity, role));
    }

    @Override
    public void leave(Long teamId, Long memberId) {
        participantJpaRepository.deleteByTeamIdAndMemberId(teamId, memberId);
    }

    @Override
    public boolean existByName(String name) {
        return teamJpaRepository.findByName(name)
                .isPresent();
    }

    @Override
    public boolean existByTeamIdAndMemberId(Long teamId, Long memberId) {
        return participantJpaRepository.existsByTeamIdAndMemberId(teamId, memberId);
    }

    @Override
    public TeamMemberRole getRole(Long teamId, Long memberId) {
        return participantJpaRepository.findByTeamIdAndMemberId(teamId, memberId)
                .getTeamMemberRole();
    }

    @Override
    public List<Team> getPublicTeamsByName(String keyword) {
        List<TeamEntity> teamEntities = teamJpaRepository.findByNameContainingAndSearchable(keyword, Searchable.PUBLIC);
        return teamEntities.stream()
                .map(TeamEntity::toTeam)
                .toList();
    }

    @Override
    public void updateSearchable(Long teamId, Searchable searchable) {
        teamJpaRepository.updateSearchable(teamId, searchable);
    }

    @Override
    public boolean isTeamMember(Long teamId, Long memberId) {
        return participantJpaRepository.existsByTeamIdAndMemberId(teamId, memberId);
    }

    @Override
    public void updateTeamMemberRole(Long teamId, Long memberId, TeamMemberRole role) {
        participantJpaRepository.updateTeamMemberRole(teamId, memberId, role);
    }

    @Override
    public void deactivateTeam(Long teamId) {
        TeamEntity teamEntity = teamJpaRepository.getReferenceById(teamId);
        teamEntity.delete();
    }

    public List<com.yellobook.domains.team.dto.query.QueryTeamMember> findTeamMembers(Long teamId) {
        QMember member = QMember.member;
        QParticipant participant = QParticipant.participant;
        QTeam team = QTeam.team;
        return queryFactory
                .select(
                        Projections.constructor(com.yellobook.domains.team.dto.query.QueryTeamMember.class,
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

    public List<com.yellobook.domains.team.dto.query.QueryTeamMember> findMentionsByNamePrefix(String prefix,
                                                                                               Long teamId) {
        QMember member = QMember.member;
        QParticipant participant = QParticipant.participant;

        return queryFactory
                .select(Projections.constructor(com.yellobook.domains.team.dto.query.QueryTeamMember.class,
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
