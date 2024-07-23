package com.yellobook.domain.inventory.service;

import com.yellobook.domain.inventory.dto.AddProductRequest;

public interface InventoryCommandService {

    void addProduct(Long teamId, Long inventoryId, AddProductRequest requestDTO);

}
