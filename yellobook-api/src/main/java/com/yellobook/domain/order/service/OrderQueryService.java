package com.yellobook.domain.order.service;

import com.yellobook.common.utils.TeamUtil;
import com.yellobook.domain.order.dto.MakeOrderRequest;
import com.yellobook.domains.order.repository.OrderRepository;
import com.yellobook.enums.MemberTeamRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderRepository orderRepository;
    private final TeamUtil teamUtil;

    public boolean existsByOrderId(Long orderId){
        return orderRepository.existsById(orderId);
    }

    public void makeOrder(MakeOrderRequest requestDTO, Long memberId) {
        // 관리자, 주문자 주문 불가능
        Long teamId = teamUtil.getCurrentTeam(memberId);
        MemberTeamRole role; // TODO (만들어 놓은거 사용) - 관리자, 주문자 주문 불가능
        // mapstruct로 주문 생성 - 작성자, 팀, 제품 FK
        // 함께하는 사용자 테이블 추가
        // save
    }
}
