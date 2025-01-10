package com.yellobook.core.domain.order;

import com.yellobook.core.domain.order.dto.CreateOrderCommend;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    Long save(CreateOrderCommend order);

    void delete(Long orderId);

    Optional<Order> findById(Long orderId);

    void updateOrderStatus(OrderStatus status, Long orderId);
}
