package com.yellobook.storage.db.core.inventory;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findFirstByTeamIdOrderByCreatedAtDesc(Long teamId);

    @Modifying
    @Query("update InventoryEntity i set i.updatedAt = :now where i.id = :id")
    void updateUpdatedAt(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update InventoryEntity i set i.view = i.view+1 where i.id = :id")
    void increaseView(@Param("id") Long id);
}
