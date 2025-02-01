//package com.yellobook.core.domain.inventory;
//
//import static com.yellobook.core.error.CoreErrorType.ORDER_RELATED;
//
//import com.yellobook.core.domain.inventory.dto.CreateProductCommend;
//import com.yellobook.core.domain.order.OrderReader;
//import com.yellobook.core.error.CoreException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ProductWriter {
//    private final OrderReader orderReader;
//    private final ProductRepository productRepository;
//
//    @Autowired
//    public ProductWriter(ProductRepository productRepository, OrderReader orderReader) {
//        this.productRepository = productRepository;
//        this.orderReader = orderReader;
//    }
//
//    public Long create(CreateProductCommend dto) {
//        return productRepository.save(dto);
//    }
//
//    public void updateAmount(int amount, Long productId) {
//        productRepository.updateAmount(amount, productId);
//    }
//
//    public void delete(Long productId) {
//        // 주문에 연관되어 있는 제품이면 삭제 불가능
//        if (orderReader.existByProduct(productId)) {
//            throw new CoreException(ORDER_RELATED);
//        }
//        productRepository.delete(productId);
//    }
//
//}
