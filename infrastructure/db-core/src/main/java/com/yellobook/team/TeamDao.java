package com.yellobook.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.*;
import com.yellobook.member.MemberEntity;
import com.yellobook.member.MemberJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TeamDao implements TeamRepository {
    private final TeamJpaRepository teamJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final TeamApplyJpaRepository teamApplyJpaRepository;
    private final TeamRoleChangeJpaRepository teamRoleChangeJpaRepository;

    public TeamDao(TeamJpaRepository teamJpaRepository, ParticipantJpaRepository participantJpaRepository,
                   MemberJpaRepository memberJpaRepository, TeamApplyJpaRepository teamApplyJpaRepository, TeamRoleChangeJpaRepository teamRoleChangeJpaRepository) {
        this.teamJpaRepository = teamJpaRepository;
        this.participantJpaRepository = participantJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.teamApplyJpaRepository = teamApplyJpaRepository;
        this.teamRoleChangeJpaRepository = teamRoleChangeJpaRepository;
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
        return teamJpaRepository.getTeamsByMemberId(memberId);
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
        return teamEntities.stream().map(TeamEntity::toTeam).toList();
    }

    @Override
    public void updateSearchable(Long teamId, Searchable searchable) {
        teamJpaRepository.updateSearchable(teamId, searchable);
    }

    @Override
    public Long applyTeam(Long teamId, Long memberId) {
        TeamEntity teamEntity = teamJpaRepository.getReferenceById(teamId);
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(memberId);
        TeamApplyEntity teamApplyEntity = new TeamApplyEntity(teamEntity, memberEntity, JoinStatus.PENDING);
        return teamApplyJpaRepository.save(teamApplyEntity).getId();
    }

    @Override
    public boolean hasAppliedTeam(Long teamId, Long memberId) {
        return teamApplyJpaRepository.existsByTeamIdAndMemberId(teamId, memberId);
    }

    @Override
    public Optional<TeamApplyInfo> findTeamApplyById(Long applyId) {
        return teamApplyJpaRepository.findById(applyId)
                .map(TeamApplyEntity::toTeamApplyInfo);
    }

    @Override
    public void updateJoinStatus(Long applyId, JoinStatus joinStatus) {
        teamApplyJpaRepository.updateJoinStatus(applyId, joinStatus);
    }

    @Override
    public boolean hasRequestedOrdererConversion(Long teamId, Long memberId) {
        return teamRoleChangeJpaRepository.existsByTeamIdAndMemberIdAndRequestRole(teamId, memberId, TeamMemberRole.ORDERER);
    }

    @Override
    public Long requestOrdererConversion(Long teamId, Long memberId) {
        TeamEntity teamEntity = teamJpaRepository.getReferenceById(teamId);
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(memberId);
        TeamRoleChangeEntity teamRoleChangeEntity = new TeamRoleChangeEntity(
                teamEntity,
                memberEntity,
                TeamMemberRole.ORDERER,
                ChangeRoleStatus.PENDING
        );
        return teamRoleChangeJpaRepository.save(teamRoleChangeEntity).getId();
    }

    @Override
    public Optional<RoleConversionInfo> findTeamRoleConversionById(Long conversionId) {
        return teamRoleChangeJpaRepository.findById(conversionId)
                .map(TeamRoleChangeEntity::toRoleConversionInfo);
    }

    @Override
    public void updateRoleConversionStatus(Long requestId, ChangeRoleStatus status) {
        teamRoleChangeJpaRepository.updateChangeRoleStatus(requestId, status);
    }

    @Override
    public void updateTeamMemberRole(Long teamId, Long memberId, TeamMemberRole role) {
        participantJpaRepository.updateTeamMemberRole(teamId, memberId, role);
    }

}
