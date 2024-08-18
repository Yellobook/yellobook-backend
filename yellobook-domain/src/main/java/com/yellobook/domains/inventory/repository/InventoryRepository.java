package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  InventoryRepository extends JpaRepository<Inventory, Long>, InventoryRepositoryCustom {
    Optional<Inventory> findFirstByTeamIdOrderByCreatedAtDesc(Long teamId);
}
