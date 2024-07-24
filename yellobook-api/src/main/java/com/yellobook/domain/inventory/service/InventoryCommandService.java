package com.yellobook.domain.inventory.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryCommandService{
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;


    public void addProduct(Long inventoryId, AddProductRequest requestDTO, CustomOAuth2User oAuth2User) {
        // 연관관계 추가하고, 제품 등록
        //productRepository.save(newProduct);
    }

    public void modifyProductAmount(Long productId, ModifyProductAmountRequest requestDTO, CustomOAuth2User user) {
        // 수정하고, dirty checking
    }

    public void deleteProduct(Long productId, CustomOAuth2User user) {
        //productRepository.deleteById(productId);
    }

}
