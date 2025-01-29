package com.yellobook.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ParticipantJpaRepository extends JpaRepository<Participant, Long>, ParticipantCustomRepository {
    Optional<Participant> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);

    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);

    void deleteByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<Participant> findByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole teamMemberRole);

    boolean existsByTeamIdAndMemberIdAndTeamMemberRole(Long teamId, Long memberId,
                                                       TeamMemberRole teamMemberRole);

    boolean existsByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole role);

    Participant findByTeamIdAndMemberId(Long teamId, Long memberId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update Participant p set p.teamMemberRole = :role where p.team.id = :teamId and p.member.id = :memberId")
    void updateTeamMemberRole(@Param("teamId") Long teamId, @Param("memberId") Long memberId, @Param("role") TeamMemberRole role);
}
