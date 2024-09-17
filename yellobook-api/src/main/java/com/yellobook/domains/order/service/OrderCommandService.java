package com.yellobook.domains.order.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.common.utils.ParticipantUtil;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.order.dto.request.AddOrderCommentRequest;
import com.yellobook.domains.order.dto.request.MakeOrderRequest;
import com.yellobook.domains.order.dto.response.AddOrderCommentResponse;
import com.yellobook.domains.order.dto.response.MakeOrderResponse;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.domains.order.entity.OrderMention;
import com.yellobook.domains.order.mapper.OrderMapper;
import com.yellobook.domains.order.repository.OrderCommentRepository;
import com.yellobook.domains.order.repository.OrderMentionRepository;
import com.yellobook.domains.order.repository.OrderRepository;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.InventoryErrorCode;
import com.yellobook.error.code.MemberErrorCode;
import com.yellobook.error.code.OrderErrorCode;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandService {
    private final OrderRepository orderRepository;
    private final OrderCommentRepository orderCommentRepository;
    private final OrderMentionRepository orderMentionRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ProductRepository productRepository;
    private final ParticipantRepository participantRepository;
    private final OrderMapper orderMapper;

    /**
     * 주문 정정 요청 (관리자)
     */
    public void modifyRequestOrder(Long orderId, TeamMemberVO teamMemberVO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        // 함께하는 사람에 내가 없으면 (관리자가 아니라는 뜻, 접근권한 에러)
        if (!orderMentionRepository.existsByMemberIdAndOrderId(teamMemberVO.getMemberId(), order.getId())) {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
        // 주문 확인 상태이면 변경 불가능
        if (order.getOrderStatus()
                .equals(OrderStatus.CONFIRMED)) {
            throw new CustomException(OrderErrorCode.ORDER_CONFIRMED_CANT_MODIFY);
        }
        // 수정 & dirty checking
        order.requestModifyOrder();
    }

    /**
     * 주문 확정 (관리자)
     */
    public void confirmOrder(Long orderId, TeamMemberVO teamMemberVO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        // 함께하는 사람에 내가 없으면 (관리자가 아니라는 뜻, 접근권한 에러)
        if (!orderMentionRepository.existsByMemberIdAndOrderId(teamMemberVO.getMemberId(), order.getId())) {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
        // 주문 정정 요청이 되어 있으면 주문 확정 불가능
        if (order.getOrderStatus()
                .equals(OrderStatus.PENDING_MODIFY)) {
            throw new CustomException(OrderErrorCode.ORDER_PENDING_MODIFY_CANT_CONFIRM);
        }
        // 제품 수량 마이너스
        if (order.getOrderAmount() > order.getProduct()
                .getAmount()) {
            throw new CustomException(OrderErrorCode.ORDER_AMOUNT_EXCEED);
        } else {
            order.getProduct()
                    .reduceAmount(order.getOrderAmount());
        }
        order.confirmOrder();
    }

    /**
     * 주문 취소 (주문자)
     */
    public void cancelOrder(Long orderId, TeamMemberVO teamMemberVO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        // 내가 작성한 주문이 아니면 취소 불가능
        if (!order.getMember()
                .getId()
                .equals(teamMemberVO.getMemberId())) {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
        // 주문 정정 상태가 아니면 취소 불가능
        if (!order.getOrderStatus()
                .equals(OrderStatus.PENDING_MODIFY)) {
            throw new CustomException(OrderErrorCode.ORDER_CANT_CANCEL);
        }
        // 언급한 사용자 삭제
        orderMentionRepository.deleteAllByOrderId(order.getId());
        // order 삭제
        orderRepository.delete(order);
    }

    /**
     * 주문 댓글 추가 (관리자, 주문자)
     */
    public AddOrderCommentResponse addOrderComment(Long orderId, TeamMemberVO teamMemberVO,
                                                   AddOrderCommentRequest requestDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        Member member = memberRepository.findById(teamMemberVO.getMemberId())
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        // 접근 권한 확인 : 해당 글의 주문자 인지 확인, 해당 글의 관리자 인지 확인(언급된 사람)
        if (member.getId()
                .equals(order.getMember()
                        .getId()) || orderMentionRepository.existsByMemberIdAndOrderId(member.getId(), order.getId())) {
            // 댓글 추가 (단방향)
            OrderComment comment = orderMapper.toOrderComment(requestDTO, member, order);
            Long commentId = orderCommentRepository.save(comment)
                    .getId();
            return orderMapper.toAddOrderCommentResponse(commentId);
        } else {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
    }

    /**
     * 주문 생성
     */
    public MakeOrderResponse makeOrder(MakeOrderRequest requestDTO, TeamMemberVO teamMemberVO) {
        Member member = memberRepository.findById(teamMemberVO.getMemberId())
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Team team = teamRepository.findById(teamMemberVO.getTeamId())
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));
        MemberTeamRole role = teamMemberVO.getRole();
        // 관리자, 뷰어 주문 불가능
        ParticipantUtil.forbidAdmin(role);
        ParticipantUtil.forbidViewer(role);
        // 관리자 없으면 주문자 주문 불가능
        Optional<Participant> optionalParticipant = participantRepository.findByTeamIdAndRole(team.getId(),
                MemberTeamRole.ADMIN);
        if (optionalParticipant.isEmpty()) {
            throw new CustomException(OrderErrorCode.ORDER_CREATION_NOT_ALLOWED);
        }
        Member admin = optionalParticipant.get()
                .getMember();
        // 존재하는 제품인지 확인
        Product product = productRepository.findById(requestDTO.productId())
                .orElseThrow(() -> new CustomException(InventoryErrorCode.PRODUCT_NOT_FOUND));
        // 수량 비교
        if (product.getAmount() < requestDTO.orderAmount()) {
            throw new CustomException(OrderErrorCode.ORDER_AMOUNT_EXCEED);
        }
        // mapstruct로 주문 생성 - 작성자, 팀, 제품 FK
        Order order = orderMapper.toOrder(requestDTO, member, team, product);
        // 함께하는 사용자 테이블에 관리자 추가
        OrderMention orderMention = orderMapper.toOrderMention(order, admin);
        Long orderId = orderRepository.save(order)
                .getId();
        orderMentionRepository.save(orderMention);
        return orderMapper.toMakeOrderResponse(orderId);
    }
}
