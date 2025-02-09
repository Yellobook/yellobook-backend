package com.yellobook.core.domain.order;

import static com.yellobook.core.error.CoreErrorType.ORDER_NOT_FOUND;

import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class OrderReader {
    private final OrderRepository orderRepository;

    OrderReader(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order read(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CoreException(ORDER_NOT_FOUND));
    }

    public boolean existByProduct(Long productId) {
        return orderRepository.existByProductId(productId);
    }
}
