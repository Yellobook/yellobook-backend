package com.yellobook.core.domains.inventory.repository;

import com.yellobook.core.domains.inventory.entity.Inventory;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, InventoryRepositoryCustom {
    Optional<Inventory> findFirstByTeamIdOrderByCreatedAtDesc(Long teamId);

    @Modifying
    @Query("update Inventory i set i.updatedAt = :now where i.id = :id")
    void updateUpdatedAt(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Inventory i set i.view = i.view+1 where i.id = :id")
    void increaseView(@Param("id") Long id);

}
