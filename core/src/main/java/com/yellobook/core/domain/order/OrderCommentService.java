package com.yellobook.core.domain.order;

import org.springframework.stereotype.Service;

@Service
public class OrderCommentService {
    private final OrderCommentRepository orderCommentRepository;

    public OrderCommentService(OrderCommentRepository orderCommentRepository) {
        this.orderCommentRepository = orderCommentRepository;
    }
}
