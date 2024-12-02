package com.yellobook.core.domains.inventory.repository;

import com.yellobook.core.domains.inventory.dto.query.QueryInventory;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface InventoryRepositoryCustom {
    List<QueryInventory> getTotalInventory(Long teamId, Pageable page);
}
