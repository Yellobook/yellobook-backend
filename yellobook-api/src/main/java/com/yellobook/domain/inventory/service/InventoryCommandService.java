package com.yellobook.domain.inventory.service;

import com.yellobook.domain.inventory.dto.ModifyProductRequest;

public interface InventoryCommandService {
    void modifyProduct(Long teamId, Long productId, ModifyProductRequest requestDTO);
}
