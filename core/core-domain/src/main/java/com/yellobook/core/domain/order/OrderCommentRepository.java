package com.yellobook.core.domain.order;

import com.yellobook.core.domain.order.dto.CreateOrderCommentCommend;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCommentRepository {
    Long save(CreateOrderCommentCommend dto);

    List<OrderComment> getOrderCommentsByOrderId(Long orderId);
}
