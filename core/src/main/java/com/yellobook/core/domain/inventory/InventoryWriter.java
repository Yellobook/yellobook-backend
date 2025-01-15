package com.yellobook.core.domain.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryWriter {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryWriter(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void updateUpdatedAt(Long inventoryId) {
        inventoryRepository.updateUpdatedAt(inventoryId);
    }

    public void increaseView(Long inventoryId) {
        inventoryRepository.increaseView(inventoryId);
    }

    public Long create(Long teamId, String title) {
        return inventoryRepository.save(teamId, title);
    }

}
