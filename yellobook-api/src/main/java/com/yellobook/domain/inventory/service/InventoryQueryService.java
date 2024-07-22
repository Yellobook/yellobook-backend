package com.yellobook.domain.inventory.service;

import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;

public interface InventoryQueryService {
    public GetTotalInventoryResponse getTotalInventory(Long page, Long size, Long teamId);
    public GetProductsResponse getProductByInventory(Long teamId, Long inventoryId);

    public GetProductsResponse getProductByKeywordAndInventory(Long teamId, Long inventoryId, String keyword);
}
