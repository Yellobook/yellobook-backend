package com.yellobook.domain.order.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService{
    private final OrderRepository orderRepository;

    @Override
    public void modifyRequestOrder(Long teamId, Long orderId, CustomOAuth2User oAuth2User) {
        // 수정 & dirty checking
    }

    @Override
    public void confirmOrder(Long teamId, Long orderId, CustomOAuth2User oAuth2User) {
        // 수정 & dirty checking
    }

    @Override
    public void cancelOrder(Long teamId, Long orderId, CustomOAuth2User oAuth2User) {
        // order 삭제
        //orderRepository.delete();
    }
}
