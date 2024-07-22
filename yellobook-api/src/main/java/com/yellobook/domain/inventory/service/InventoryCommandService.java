package com.yellobook.domain.inventory.service;

import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;

public interface InventoryCommandService {
    void modifyProductAmount(Long teamId, Long productId, ModifyProductAmountRequest requestDTO);

    void addProduct(Long teamId, Long inventoryId, AddProductRequest requestDTO);
}
