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
public class InventoryCommandServiceImpl implements InventoryCommandService{
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;


    @Override
    public void addProduct(Long teamId, Long inventoryId, AddProductRequest requestDTO, CustomOAuth2User oAuth2User) {
        // 연관관계 추가하고, 제품 등록
        //productRepository.save(newProduct);
    }

    @Override
    public void modifyProductAmount(Long teamId, Long productId, ModifyProductAmountRequest requestDTO, CustomOAuth2User user) {
        // 수정하고, dirty checking
    }

    @Override
    public void deleteProduct(Long teamId, Long productId, CustomOAuth2User user) {
        // 이거 productId 하나만 있어도 로직 구현 가능하기는 하는딩...
        // inventoryId는 productId validation 하는 용도로..?
        //productRepository.deleteById(productId);
    }


}
