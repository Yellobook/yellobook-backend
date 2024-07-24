package com.yellobook.domain.inventory.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;

public interface InventoryCommandService {

    void addProduct(Long teamId, Long inventoryId, AddProductRequest requestDTO, CustomOAuth2User oAuth2User);

    void modifyProductAmount(Long teamId, Long productId, ModifyProductAmountRequest requestDTO, CustomOAuth2User user);

    void deleteProduct(Long teamId, Long productId, CustomOAuth2User user);

}
