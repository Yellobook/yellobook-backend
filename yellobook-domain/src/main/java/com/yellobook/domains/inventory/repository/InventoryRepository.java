package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.entity.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, InventoryRepositoryCustom {
    Optional<Inventory> findFirstByTeamIdOrderByCreatedAtDesc(Long teamId);
}
