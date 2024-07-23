package com.yellobook.domain.order.service;

public interface OrderCommandService {
    void modifyRequestOrder(Long teamId, Long orderId);

    void confirmOrder(Long teamId, Long orderId);

    void cancelOrder(Long teamId, Long orderId);
}
