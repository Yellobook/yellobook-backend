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
public class InventoryQueryServiceImpl implements InventoryQueryService{
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public GetTotalInventoryResponse getTotalInventory(Integer page, Integer size, Long teamId) {
        // querydsl 사용 + 페이징
        // Pageable pageable = PageRequest.of(page-1, size);
        // inventoryRepository.getTotalInventory(pageable, teamId);
        return null;
    }

    @Override
    public GetProductsResponse getProductsByInventory(Long teamId, Long inventoryId) {
//         productRepository.getProducts(inventoryId);
        return null;
    }

    @Override
    public GetProductsResponse getProductByKeywordAndInventory(Long teamId, Long inventoryId, String keyword, CustomOAuth2User oAuth2User) {
//        productRepository.getProducts(inventoryId, keyword);
        return null;
    }
}
