package com.yellobook.core.domain.order;

import com.yellobook.core.domain.order.dto.CreateOrderCommentCommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderCommentWriter {
    private final OrderCommentRepository orderCommentRepository;

    @Autowired
    public OrderCommentWriter(OrderCommentRepository orderCommentRepository) {
        this.orderCommentRepository = orderCommentRepository;
    }

    public Long create(CreateOrderCommentCommend dto) {
        return orderCommentRepository.save(dto);
    }
}
