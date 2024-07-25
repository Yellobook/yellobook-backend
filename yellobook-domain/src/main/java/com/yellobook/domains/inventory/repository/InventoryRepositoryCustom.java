package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.InventoryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryRepositoryCustom {
    List<InventoryDTO> getTotalInventory(Long teamId, Pageable page);
}
