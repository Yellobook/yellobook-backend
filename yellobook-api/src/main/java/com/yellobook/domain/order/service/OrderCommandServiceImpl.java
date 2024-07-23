package com.yellobook.domain.order.service;

import com.yellobook.domains.post.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService{
    private final OrderRepository orderRepository;

    @Override
    public void modifyRequestOrder(Long teamId, Long orderId) {
        // 수정 & dirty checking
    }

    @Override
    public void confirmOrder(Long teamId, Long orderId) {
        // 수정 & dirty checking
    }

    @Override
    public void cancelOrder(Long teamId, Long orderId) {
        // order 삭제
        //orderRepository.delete();
    }
}
