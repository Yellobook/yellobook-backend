package com.yellobook.team;

import com.yellobook.core.domain.team.Searchable;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long>, TeamCustomRepository {
    Optional<TeamEntity> findByName(String name);

    List<QueryTeamMember> findTeamMembers(Long teamId);

    List<TeamEntity> findByNameContainingAndSearchable(String keyword, Searchable searchable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update TeamEntity t set t.searchable = :searchable where t.id = :teamId")
    void updateSearchable(@Param("teamId") Long teamId, @Param("searchable") Searchable searchable);
}
