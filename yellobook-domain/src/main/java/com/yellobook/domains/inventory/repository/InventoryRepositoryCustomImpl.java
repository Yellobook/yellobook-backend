package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.InventoryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom{
    @Override
    public List<InventoryDTO> getTotalInventory(Pageable page, Long teamId) {
        return null;
    }
}
