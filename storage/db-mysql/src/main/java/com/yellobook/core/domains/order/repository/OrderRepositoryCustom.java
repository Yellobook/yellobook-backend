package com.yellobook.core.domains.order.repository;

import com.yellobook.core.domains.order.dto.query.QueryOrder;
import com.yellobook.core.domains.order.dto.query.QueryOrderComment;
import java.util.List;

public interface OrderRepositoryCustom {

    List<QueryOrderComment> getOrderComments(Long orderId);

    QueryOrder getOrder(Long orderId);
}
