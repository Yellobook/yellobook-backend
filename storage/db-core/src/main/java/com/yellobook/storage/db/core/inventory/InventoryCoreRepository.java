package com.yellobook.storage.db.core.inventory;

import com.yellobook.core.domain.inventory.Inventory;
import com.yellobook.core.domain.inventory.InventoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryCoreRepository implements InventoryRepository {

    @Override
    public Optional<Inventory> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Long save(Long teamId, String title) {
        return 1L;
    }

    @Override
    public List<Inventory> findInventoriesByTeamId(Long teamId, Integer page, Integer size) {
        return List.of();
    }

    @Override
    public Optional<Inventory> findLastByTeamIdAndCreatedAt(Long teamId) {
        return Optional.empty();
    }

    @Override
    public void updateUpdatedAt(Long id) {

    }

    @Override
    public void increaseView(Long id) {

    }
}



