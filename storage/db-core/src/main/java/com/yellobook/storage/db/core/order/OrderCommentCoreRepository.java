package com.yellobook.storage.db.core.order;

import com.yellobook.core.domain.order.OrderComment;
import com.yellobook.core.domain.order.OrderCommentRepository;
import com.yellobook.core.domain.order.dto.CreateOrderCommentCommend;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCommentCoreRepository implements OrderCommentRepository {

    @Override
    public Long save(CreateOrderCommentCommend dto) {
        return 1L;
    }

    @Override
    public List<OrderComment> getOrderCommentsByOrderId(Long orderId) {
        return List.of();
    }
}
