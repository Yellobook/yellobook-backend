package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.query.QueryInventory;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface InventoryRepositoryCustom {
    List<QueryInventory> getTotalInventory(Long teamId, Pageable page);
}
