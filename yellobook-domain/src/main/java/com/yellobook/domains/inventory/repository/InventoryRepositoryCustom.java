package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.InventoryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryRepositoryCustom {
    List<InventoryDto> getTotalInventory(Pageable page, Long teamId);
}
