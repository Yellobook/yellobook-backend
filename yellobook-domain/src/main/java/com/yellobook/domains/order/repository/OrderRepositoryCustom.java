package com.yellobook.domains.order.repository;

import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;

import java.util.List;

public interface OrderRepositoryCustom {

    List<QueryOrderComment> getOrderComments(Long orderId);

    QueryOrder getOrder(Long orderId);
}
