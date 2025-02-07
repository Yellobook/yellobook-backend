//package com.yellobook.core.domain.inventory;
//
//import static com.yellobook.core.error.CoreErrorType.PRODUCT_SKU_ALREADY_EXISTS;
//
//import com.yellobook.core.error.CoreErrorType;
//import com.yellobook.core.error.CoreException;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ProductReader {
//    private final ProductRepository productRepository;
//
//    @Autowired
//    public ProductReader(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }
//
//    public Product read(Long productId) {
//        return productRepository.findById(productId)
//                .orElseThrow(
//                        () -> new CoreException(CoreErrorType.PRODUCT_NOT_FOUND)
//                );
//    }
//
//    public List<Product> readProductsByInventoryId(Long inventoryId) {
//        return productRepository.findProductsByInventoryId(inventoryId);
//    }
//
//
//    public List<Product> readProductsByInventoryIdAndKeyword(Long inventoryId, String keyword) {
//        return productRepository.findProductsByInventoryIdAndKeyword(inventoryId, keyword);
//    }
//
//    public List<Product> readProductsByInventoryIdAndName(Long inventoryId, String name) {
//        return productRepository.findProductsByInventoryIdAndName(inventoryId, name);
//    }
//
//    // SKU 중복 확인 (inventory & sku)
//    public void existByInventoryIdAndSku(Long inventoryId, int sku) {
//        if (productRepository.existsByInventoryIdAndSku(inventoryId, sku)) {
//            throw new CoreException(PRODUCT_SKU_ALREADY_EXISTS);
//        }
//    }
//
//
//}
