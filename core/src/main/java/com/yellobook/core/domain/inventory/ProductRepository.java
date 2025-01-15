package com.yellobook.core.domain.inventory;

import com.yellobook.core.domain.inventory.dto.CreateProductCommend;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {
    Optional<Product> findById(Long productId);

    Long save(CreateProductCommend dto);

    void updateAmount(int amount, Long productId);

    // 제품 이름 순으로 정렬해서 보여주기 (페이징 X)
    List<Product> findProductsByInventoryId(Long inventoryId);

    // 제품 이름 순으로 정렬해서 보여주기 (페이징 X)
    List<Product> findProductsByInventoryIdAndKeyword(Long inventoryId, String keyword);

    List<Product> findProductsByInventoryIdAndName(Long inventoryId, String name);

    boolean existsByInventoryIdAndSku(Long inventoryId, int sku);

    void delete(Long id);
}
