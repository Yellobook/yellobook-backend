package com.yellobook.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.team.ChangeRoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRoleChangeJpaRepository extends JpaRepository<TeamRoleChangeEntity, Long> {
    boolean existsByTeamIdAndMemberIdAndRequestRole(Long teamId, Long memberId, TeamMemberRole role);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update TeamRoleChangeEntity t set t.status = :status where t.id = :id")
    void updateChangeRoleStatus(@Param("id") Long id, @Param("status") ChangeRoleStatus status);
}
