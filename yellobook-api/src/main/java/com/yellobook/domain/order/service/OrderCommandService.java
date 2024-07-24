package com.yellobook.domain.order.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandService{
    private final OrderRepository orderRepository;

    public void modifyRequestOrder(Long orderId, CustomOAuth2User oAuth2User) {
        // 수정 & dirty checking
    }

    public void confirmOrder(Long orderId, CustomOAuth2User oAuth2User) {
        // 수정 & dirty checking
    }

    public void cancelOrder(Long orderId, CustomOAuth2User oAuth2User) {
        // order 삭제
        //orderRepository.delete();
    }
}
