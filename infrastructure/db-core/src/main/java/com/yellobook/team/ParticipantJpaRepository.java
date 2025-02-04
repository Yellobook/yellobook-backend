package com.yellobook.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ParticipantJpaRepository extends JpaRepository<ParticipantEntity, Long> {
    Optional<ParticipantEntity> findFirstByMemberIdOrderByCreatedAtAsc(Long memberId);

    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);

    void deleteByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<ParticipantEntity> findByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole teamMemberRole);

    boolean existsByTeamIdAndMemberIdAndTeamMemberRole(Long teamId, Long memberId,
                                                       TeamMemberRole teamMemberRole);

    boolean existsByTeamIdAndTeamMemberRole(Long teamId, TeamMemberRole role);

    ParticipantEntity findByTeamIdAndMemberId(Long teamId, Long memberId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update ParticipantEntity p set p.teamMemberRole = :role where p.team.id = :teamId and p.member.id = :memberId")
    void updateTeamMemberRole(@Param("teamId") Long teamId, @Param("memberId") Long memberId,
                              @Param("role") TeamMemberRole role);

    List<ParticipantEntity> findAllByTeamId(Long teamId);
}
