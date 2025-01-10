package com.yellobook.core.domain.order;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderCommentReader {
    private final OrderCommentRepository orderCommentRepository;

    @Autowired
    public OrderCommentReader(OrderCommentRepository orderCommentRepository) {
        this.orderCommentRepository = orderCommentRepository;
    }

    public List<OrderComment> readByOrderId(Long orderId) {
        return orderCommentRepository.getOrderCommentsByOrderId(orderId);
    }
}
