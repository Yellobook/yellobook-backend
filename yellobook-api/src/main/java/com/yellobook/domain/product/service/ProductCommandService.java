package com.yellobook.domain.product.service;

import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;

public interface ProductCommandService {

    void modifyProductAmount(Long teamId, Long productId, ModifyProductAmountRequest requestDTO);

    void deleteProduct(Long teamId, Long productId);
}
