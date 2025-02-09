package com.yellobook.storage.db.core.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.team.Participant;
import com.yellobook.core.domain.team.Team;
import com.yellobook.core.domain.team.TeamRepository;
import com.yellobook.core.enums.TeamMemberRole;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
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
        QTeamEntity team = QTeamEntity.teamEntity;
        QParticipantEntity participant = QParticipantEntity.participantEntity;

        List<TeamEntity> teamEntities = queryFactory
                .selectFrom(team)
                .join(participant)
                .on(participant.team.id.eq(team.id))
                .where(participant.member.id.eq(memberId))
                .fetch();

        return teamEntities.stream()
                .map(TeamEntity::toTeam)
                .toList();
    }

    @Override
    public Long save(String name, String description, String phoneNumber, String address, Boolean searchable) {
        TeamEntity team = new TeamEntity(name, description, phoneNumber, address, searchable);
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
    public List<Team> getSearchableTeamsByName(String keyword) {
        List<TeamEntity> teamEntities = teamJpaRepository.findAllByNameContainingAndSearchableIsTrue(keyword);
        return teamEntities.stream()
                .map(TeamEntity::toTeam)
                .toList();
    }

    @Override
    public void updateSearchable(Long teamId, Boolean searchable) {
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
    public int countAllByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole role) {
        return participantJpaRepository.countAllByTeamIdAndTeamMemberRole(teamId, role);
    }


}
