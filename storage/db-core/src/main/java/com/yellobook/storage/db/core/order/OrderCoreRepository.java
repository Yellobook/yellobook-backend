package com.yellobook.storage.db.core.order;

import com.yellobook.core.domain.order.Order;
import com.yellobook.core.domain.order.OrderRepository;
import com.yellobook.core.domain.order.OrderStatus;
import com.yellobook.core.domain.order.dto.CreateOrderCommend;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCoreRepository implements OrderRepository {
    @Override
    public Long save(CreateOrderCommend order) {
        return 1L;
    }

    @Override
    public void delete(Long orderId) {

    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.empty();
    }

    @Override
    public void updateOrderStatus(OrderStatus status, Long orderId) {

    }

    @Override
    public boolean existByProductId(Long productId) {
        return false;
    }
}
