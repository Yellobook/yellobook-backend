package com.yellobook.domain.inventory.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;

public interface InventoryQueryService {
    public GetTotalInventoryResponse getTotalInventory(Integer page, Integer size, Long teamId);
    public GetProductsResponse getProductsByInventory(Long teamId, Long inventoryId);

    public GetProductsResponse getProductByKeywordAndInventory(Long teamId, Long inventoryId, String keyword, CustomOAuth2User oAuth2User);
}
