package com.yellobook.domain.order.service;

import com.yellobook.common.utils.TeamUtil;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.order.dto.AddOrderComment;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.repository.OrderCommentRepository;
import com.yellobook.domains.order.repository.OrderMentionRepository;
import com.yellobook.domains.order.repository.OrderRepository;
import com.yellobook.enums.MemberTeamRole;
import com.yellobook.enums.OrderStatus;
import com.yellobook.error.code.OrderErrorCode;
import com.yellobook.error.exception.CustomException;
import io.netty.resolver.InetNameResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandService{
    private final OrderRepository orderRepository;
    private final OrderCommentRepository orderCommentRepository;
    private final OrderMentionRepository orderMentionRepository;

    /**
     * 주문 정정 요청 (관리자)
     */
    public void modifyRequestOrder(Long orderId, Long memberId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        // 내가 작성한 주문이 아니면 (접근 권한 에러)
        if(!order.getMember().getId().equals(memberId)){
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
        // 주문 확인 상태이면 변경 불가능
        if(order.getOrderStatus().equals(OrderStatus.CONFIRMED)){
            throw new CustomException(OrderErrorCode.ORDER_CONFIRMED_CANT_MODIFY);
        }
        // 수정 & dirty checking
        order.requestModifyOrder();
    }

    /**
     * 주문 확정 (관리자)
     */
    public void confirmOrder(Long orderId, Long memberId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        // 함께하는 사람에 내가 없으면 (관리자가 아니라는 뜻, 접근권한 에러)
        if(orderMentionRepository.existsByMemberIdAndOrderId(memberId, order.getId())){
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
        // 주문 주문 정정 요청이 되어 있으면 주문 확정 불가능
        if(order.getOrderStatus().equals(OrderStatus.PENDING_MODIFY)){
            throw new CustomException(OrderErrorCode.ORDER_PENDING_MODIFY_CANT_CONFIRM);
        }
        // 수정 & dirty checking
        order.confirmOrder();
    }

    /**
     * 주문 취소 (주문자)
     */
    public void cancelOrder(Long orderId, Long memberId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        // 내가 작성한 주문이 아니면 취소 불가능
        if(!order.getMember().getId().equals(memberId)){
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
        // 주문 정정 상태가 아니면 취소 불가능
        if(!order.getOrderStatus().equals(OrderStatus.PENDING_MODIFY)){
            throw new CustomException(OrderErrorCode.ORDER_CANT_CANCEL);
        }
        // 언급한 사용자 삭제
        orderMentionRepository.deleteAllByOrderId(orderId);
        // order 삭제
        orderRepository.delete(order);
    }

    /**
     * 주문 댓글 추가 (관리자, 주문자)
     */
    public void addOrderComment(Long orderId, Long memberId, AddOrderComment requestDTO) {
        // 권한 확인(팀 존재 확인, 해당 글의 주문자 인지 확인, 해당 글의 관리자 인지 확인)


    }
}
