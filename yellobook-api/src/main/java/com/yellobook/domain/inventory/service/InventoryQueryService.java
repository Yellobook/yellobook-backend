package com.yellobook.domain.inventory.service;

import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;

public interface InventoryQueryService {
    public GetTotalInventoryResponse getTotalInventory(Long page, Long size, Long teamId);
    public GetProductsResponse getOneInventory(Long teamId, Long inventoryId);
}
