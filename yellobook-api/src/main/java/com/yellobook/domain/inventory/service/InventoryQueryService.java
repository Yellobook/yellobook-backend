package com.yellobook.domain.inventory.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryQueryService{
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public GetTotalInventoryResponse getTotalInventory(Integer page, Integer size, CustomOAuth2User user) {
        // querydsl 사용 + 페이징
        // Pageable pageable = PageRequest.of(page-1, size);
        // inventoryRepository.getTotalInventory(pageable, teamId);
        return null;
    }

    public GetProductsResponse getProductsByInventory(Long inventoryId) {
//         productRepository.getProducts(inventoryId);
        return null;
    }

    public GetProductsResponse getProductByKeywordAndInventory(Long inventoryId, String keyword, CustomOAuth2User oAuth2User) {
//        productRepository.getProducts(inventoryId, keyword);
        return null;
    }
}
