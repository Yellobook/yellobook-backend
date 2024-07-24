package com.yellobook.domain.order.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;

public interface OrderCommandService {
    void modifyRequestOrder(Long teamId, Long orderId, CustomOAuth2User oAuth2User);

    void confirmOrder(Long teamId, Long orderId, CustomOAuth2User oAuth2User);

    void cancelOrder(Long teamId, Long orderId, CustomOAuth2User oAuth2User);
}
