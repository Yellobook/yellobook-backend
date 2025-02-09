package com.yellobook.core.domain.inventory;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository {
    Optional<Inventory> findById(Long id);

    Long save(Long teamId, String title);

    List<Inventory> findInventoriesByTeamId(Long teamId, Integer page, Integer size);

    Optional<Inventory> findLastByTeamIdAndCreatedAt(Long teamId);

    void updateUpdatedAt(Long id);

    void increaseView(Long id);
}
