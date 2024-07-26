package com.yellobook.domains.order.repository;

import com.yellobook.domains.order.dto.OrderCommentDTO;

import java.util.List;

public interface OrderRepositoryCustom {

    List<OrderCommentDTO> getOrderComments(Long orderId);
}
