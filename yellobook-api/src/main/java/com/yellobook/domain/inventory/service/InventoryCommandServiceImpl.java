package com.yellobook.domain.inventory.service;

import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.product.repository.ProductRepository;
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
    public void addProduct(Long teamId, Long inventoryId, AddProductRequest requestDTO) {
        // 연관관계 추가하고, 제품 등록
        //productRepository.save(newProduct);
    }


}
