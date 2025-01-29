package com.yellobook.team;

import com.yellobook.core.domain.team.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamApplyJpaRepository extends JpaRepository<TeamApplyEntity, Long> {
    boolean existsByTeamIdAndMemberId(Long teamId, Long memberId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update TeamApplyEntity t set t.status = :status where t.id = :applyId")
    void updateJoinStatus(@Param("applyId") Long applyId, @Param("status") JoinStatus status);
}
