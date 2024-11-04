package com.yellobook.domains.order.service;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domains.order.dto.response.GetOrderResponse;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.mapper.OrderMapper;
import com.yellobook.domains.order.repository.OrderMentionRepository;
import com.yellobook.domains.order.repository.OrderRepository;
import com.yellobook.error.code.OrderErrorCode;
import com.yellobook.error.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderRepository orderRepository;
    private final OrderMentionRepository orderMentionRepository;
    private final OrderMapper orderMapper;

    /**
     * 주문 댓글 조회
     */
    public GetOrderCommentsResponse getOrderComments(Long orderId, TeamMemberVO teamMemberVO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        // 접근 권한 있는지 확인
        if (teamMemberVO.getMemberId()
                .equals(order.getMember()
                        .getId()) || orderMentionRepository.existsByMemberIdAndOrderId(teamMemberVO.getMemberId(),
                orderId)) {
            // 댓글 조회
            List<QueryOrderComment> orderComments = orderRepository.getOrderComments(orderId);
            return orderMapper.toGetOrderCommentsResponse(orderId, orderComments);
        } else {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
    }

    /**
     * 주문 조회
     */
    public GetOrderResponse getOrder(Long orderId, TeamMemberVO teamMemberVO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        // 접근 권한 있는지 확인
        if (teamMemberVO.getMemberId()
                .equals(order.getMember()
                        .getId()) || orderMentionRepository.existsByMemberIdAndOrderId(teamMemberVO.getMemberId(),
                orderId)) {
            // 주문 조회
            QueryOrder queryOrder = orderRepository.getOrder(orderId);
            return orderMapper.toGetOrderResponse(queryOrder);
        } else {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
    }

    public boolean existsByOrderId(Long orderId) {
        return orderRepository.existsById(orderId);
    }

}
