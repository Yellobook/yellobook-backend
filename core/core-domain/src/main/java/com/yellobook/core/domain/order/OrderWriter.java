package com.yellobook.core.domain.order;

import com.yellobook.core.domain.order.dto.CreateOrderCommend;
import com.yellobook.core.domain.order.dto.CreateOrderPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderWriter {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderWriter(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void delete(Order order) {
        orderRepository.delete(order.orderId());
    }

    public Long create(CreateOrderPayload dto, Long memberId, Long teamId) {
        return orderRepository.save(new CreateOrderCommend(
                dto.memo(),
                dto.date(),
                dto.orderAmount(),
                dto.productId(),
                0,
                OrderStatus.PENDING_CONFIRM,
                memberId,
                teamId
        ));
    }

    public void updateOrderStatus(Order order, OrderStatus status) {
        orderRepository.updateOrderStatus(status, order.orderId());
    }

}
