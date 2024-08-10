package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.query.QueryInventory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryRepositoryCustom {
    List<QueryInventory> getTotalInventory(Long teamId, Pageable page);
}
