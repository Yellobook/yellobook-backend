package com.yellobook.domains.order.repository;

import com.yellobook.domains.order.dto.OrderCommentDTO;
import com.yellobook.domains.order.dto.OrderDTO;

import java.util.List;

public interface OrderRepositoryCustom {

    List<OrderCommentDTO> getOrderComments(Long orderId);

    OrderDTO getOrder(Long orderId);
}
