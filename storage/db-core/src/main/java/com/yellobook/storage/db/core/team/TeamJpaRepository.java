package com.yellobook.storage.db.core.team;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findByName(String name);

    List<TeamEntity> findAllByNameContainingAndSearchableIsTrue(String keyword);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update TeamEntity t set t.isSearchable = :searchable where t.id = :teamId")
    void updateSearchable(@Param("teamId") Long teamId, @Param("searchable") Boolean searchable);

}
