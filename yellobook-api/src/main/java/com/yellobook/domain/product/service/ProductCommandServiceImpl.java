package com.yellobook.domain.product.service;

import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;
import com.yellobook.domains.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCommandServiceImpl implements ProductCommandService{
    private final ProductRepository productRepository;

    @Override
    public void modifyProductAmount(Long teamId, Long productId, ModifyProductAmountRequest requestDTO) {
        // 수정하고, dirty checking
    }

    @Override
    public void deleteProduct(Long teamId, Long productId) {
        // 이거 productId 하나만 있어도 로직 구현 가능하기는 하는딩...
        // inventoryId는 productId validation 하는 용도로..?
        //productRepository.deleteById(productId);
    }
}
