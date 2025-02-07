//package com.yellobook.core.domain.order;
//
//import com.yellobook.core.domain.common.TeamMemberRole;
//import com.yellobook.core.domain.order.dto.CreateOrderCommentCommend;
//import com.yellobook.core.domain.order.dto.CreateOrderPayload;
//import jakarta.transaction.Transactional;
//import java.util.List;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@Transactional
//public class OrderService {
//    private final OrderReader orderReader;
//    private final OrderWriter orderWriter;
//    private final OrderManager orderManager;
//    private final OrderPermission orderPermission;
//    private final OrderCommentReader orderCommentReader;
//    private final OrderCommentWriter orderCommentWriter;
//
//
//    @Autowired
//    OrderService(OrderReader orderReader, OrderWriter orderWriter,
//                 OrderManager orderManager,
//                 OrderPermission orderPermission, OrderCommentReader orderCommentReader,
//                 OrderCommentWriter orderCommentWriter) {
//        this.orderReader = orderReader;
//        this.orderWriter = orderWriter;
//        this.orderManager = orderManager;
//        this.orderPermission = orderPermission;
//        this.orderCommentReader = orderCommentReader;
//        this.orderCommentWriter = orderCommentWriter;
//    }
//
//    /**
//     * 주문 정정 요청 (관리자)
//     *
//     * @param orderId  주문 ID
//     * @param memberId 사용자 ID
//     * @param teamId   팀 ID
//     * @param role     팀에서 역할
//     */
//    public void adminRequestOrderModification(Long orderId, Long memberId, Long teamId, TeamMemberRole role) {
//        Order order = orderReader.read(orderId);
//        orderPermission.onlyAdminCanAccess(role);
//        orderManager.requestModify(order);
//        orderWriter.updateOrderStatus(order, OrderStatus.PENDING_MODIFY);
//    }
//
//    /**
//     * 주문 정정 요청, CONFIRMED 으로 변경 (관리자)
//     *
//     * @param orderId  주문 ID
//     * @param memberId 사용자 ID
//     * @param teamId   팀 ID
//     * @param role     팀에서 역할
//     */
//    public void adminConfirmOrder(Long orderId, Long memberId, Long teamId, TeamMemberRole role) {
//        Order order = orderReader.read(orderId);
//        orderPermission.onlyAdminCanAccess(role);
//        orderManager.canConfirmOrder(order);
//        orderWriter.updateOrderStatus(order, OrderStatus.CONFIRMED);
//        orderManager.restoreProductAmount(order);
//    }
//
//    /**
//     * 주문 취소 (주문자)
//     *
//     * @param orderId  주문 ID
//     * @param memberId 사용자 ID
//     */
//    public void ordererCancelOrder(Long orderId, Long memberId) {
//        Order order = orderReader.read(orderId);
//        orderPermission.onlyOrdererCanAccess(order, memberId);
//        orderManager.canCancelOrder(order);
//        orderManager.reduceProductAmount(order);
//        orderWriter.delete(order);
//    }
//
//    /**
//     * 주문 댓글 추가 (관리자, 주문의 주문자)
//     *
//     * @param memberId 사용자 ID
//     * @param role     팀에서 역할
//     * @param dto      주문 댓글 DTO
//     * @return 생성된 댓글의 ID
//     */
//    public Long addOrderComment(Long memberId, TeamMemberRole role, CreateOrderCommentCommend dto) {
//        Order order = orderReader.read(dto.orderId());
//        orderPermission.adminAndOrdererCanAccess(order, memberId, role);
//        return orderCommentWriter.create(dto);
//    }
//
//    /**
//     * 주문 생성 (주문자)
//     *
//     * @param memberId 사용자 ID
//     * @param teamId   팀 ID
//     * @param role     팀에서 역할
//     * @param dto      주문 DTO
//     * @return 생성된 주문의 ID
//     */
//    public Long createOrder(Long memberId, Long teamId, TeamMemberRole role, CreateOrderPayload dto) {
//        orderPermission.onlyOrdererCanOrder(role);
//        // 관리자 없으면 주문자 주문 불가능
//        orderPermission.cannotOrderWithoutAdmin(teamId);
//        orderManager.existProduct(dto.productId());
//        int productAmount = orderManager.readProductAmount(dto.productId());
//        // 수량 비교
//        orderManager.isOrderAmountExceedProductAmount(dto.orderAmount(), productAmount);
//        return orderWriter.create(dto, memberId, teamId);
//    }
//
//    /**
//     * 주문 댓글 조회 (관리자, 주문의 주문자)
//     *
//     * @param orderId  주문 ID
//     * @param memberId 사용자 ID
//     * @param teamId   팀 ID
//     * @param role     팀에서 역할
//     * @return 댓글 리스트
//     */
//    public List<OrderComment> readOrderComments(Long orderId, Long memberId, Long teamId, TeamMemberRole role) {
//        Order order = orderReader.read(orderId);
//        orderPermission.adminAndOrdererCanAccess(order, memberId, role);
//        return orderCommentReader.readByOrderId(order.orderId());
//    }
//
//    /**
//     * 주문 조회
//     *
//     * @param orderId  주문 ID
//     * @param memberId 사용자 ID
//     * @param teamId   팀 ID
//     * @param role     팀에서 역할
//     * @return 주문
//     */
//    public Order readOrder(Long orderId, Long memberId, Long teamId, TeamMemberRole role) {
//        Order order = orderReader.read(orderId);
//        orderPermission.adminAndOrdererCanAccess(order, memberId, role);
//        return order;
//    }
//
//}
