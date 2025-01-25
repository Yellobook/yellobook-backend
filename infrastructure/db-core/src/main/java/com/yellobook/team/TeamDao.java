package com.yellobook.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.Team;
import com.yellobook.core.domain.team.TeamRepository;
import com.yellobook.member.MemberEntity;
import com.yellobook.member.MemberJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TeamDao implements TeamRepository {
    private final TeamJpaRepository teamJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public TeamDao(TeamJpaRepository teamJpaRepository, ParticipantJpaRepository participantJpaRepository,
                   MemberJpaRepository memberJpaRepository) {
        this.teamJpaRepository = teamJpaRepository;
        this.participantJpaRepository = participantJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public boolean existByTeamAndMemberAndRole(Long teamId, Long memberId, TeamMemberRole role) {
        return participantJpaRepository.existsByTeamEntityIdAndMemberEntityIdAndTeamMemberRole(teamId, memberId, role);
    }

    @Override
    public boolean existByTeamAndRole(Long teamId, TeamMemberRole role) {
        return participantJpaRepository.existsByTeamEntityIdAndTeamMemberRole(teamId, role);
    }

    @Override
    public List<Team> getTeamsByMemberId(Long memberId) {
        return teamJpaRepository.getTeamsByMemberId(memberId);
    }

    @Override
    public Long save(String name, String phoneNumber, String address) {
        TeamEntity team = TeamEntity.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
        return teamJpaRepository.save(team)
                .getId();
    }

    @Override
    public Optional<Team> findById(Long teamId) {
        return teamJpaRepository.findById(teamId)
                .map(TeamEntity::toTeam);
    }

//    @Override
//    public List<Member> getMembersByTeamId(Long teamId) {
//        return teamJpaRepository.getParticipantsByTeamId(teamId);
//    }

    @Override
    public void join(Long teamId, Member member, TeamMemberRole role) {
        TeamEntity teamEntity = teamJpaRepository.getReferenceById(teamId);
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(member.memberId());
        participantJpaRepository.save(new Participant(teamEntity, memberEntity, role));
    }

    @Override
    public void leave(Long teamId, Member member) {
        participantJpaRepository.deleteByTeamIdAndMemberId(teamId, member.memberId());
    }

    @Override
    public boolean existByName(String name) {
        return teamJpaRepository.findByName(name)
                .isPresent();
    }

    @Override
    public boolean existByTeamAndMemberId(Long teamId, Member member) {
        return participantJpaRepository.existsByTeamIdAndMemberId(teamId, member.memberId());
    }

    @Override
    public TeamMemberRole getRole(Long teamId, Long memberId) {
        return participantJpaRepository.findByTeamIdAndMemberId(teamId, memberId)
                .getTeamMemberRole();
    }
}
