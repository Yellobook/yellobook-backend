//package com.yellobook.core.domain.order;
//
//import static com.yellobook.core.error.CoreErrorType.ONLY_ORDERER_CAN_ORDER;
//import static com.yellobook.core.error.CoreErrorType.ORDER_ACCESS_DENIED;
//import static com.yellobook.core.error.CoreErrorType.ORDER_CREATION_NOT_ALLOWED;
//
//import com.yellobook.core.domain.common.TeamMemberRole;
//import com.yellobook.core.domain.team.TeamRoleVerifier;
//import com.yellobook.core.error.CoreException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OrderPermission {
//    private final TeamRoleVerifier teamRoleVerifier;
//
//    @Autowired
//    public OrderPermission(TeamRoleVerifier teamRoleVerifier) {
//        this.teamRoleVerifier = teamRoleVerifier;
//    }
//
//    // 관리자만 접근 가능
//    public void onlyAdminCanAccess(TeamMemberRole role) {
//        if (!isAdmin(role)) {
//            throw new CoreException(ORDER_ACCESS_DENIED);
//        }
//    }
//
//    // 주문의 주문자만 접근 가능
//    public void onlyOrdererCanAccess(Order order, Long memberId) {
//        if (!isOrdererOfOrder(order, memberId)) {
//            throw new CoreException(ORDER_ACCESS_DENIED);
//        }
//    }
//
//    public void onlyOrdererCanOrder(TeamMemberRole role) {
//        if (!isOrderer(role)) {
//            throw new CoreException(ONLY_ORDERER_CAN_ORDER);
//        }
//    }
//
//    /**
//     * 해당 글의 주문자 또는 관리자만 접근 가능 (특정 주문 글에 접근 할 수 있는지)
//     *
//     * @param order    Order 도메인 객체
//     * @param memberId 사용자 ID
//     * @param role     팀 권한
//     */
//    public void adminAndOrdererCanAccess(Order order, Long memberId, TeamMemberRole role) {
//        if (!isOrdererOfOrder(order, memberId) && !isAdmin(role)) {
//            throw new CoreException(ORDER_ACCESS_DENIED);
//        }
//    }
//
//    /**
//     * 팀에 ADMIN 이 없으면 주문 불가능
//     *
//     * @param teamId 팀 ID
//     */
//    public void cannotOrderWithoutAdmin(Long teamId) {
//        if (!hasAdminInTeam(teamId)) {
//            throw new CoreException(ORDER_CREATION_NOT_ALLOWED);
//        }
//    }
//
//
//    private boolean isAdmin(TeamMemberRole role) {
//        return role.equals(TeamMemberRole.ADMIN);
//    }
//
//    private boolean isOrderer(TeamMemberRole role) {
//        return role.equals(TeamMemberRole.ORDERER);
//    }
//
//    // 주문의 주문자 인지 확인
//    private boolean isOrdererOfOrder(Order order, Long memberId) {
//        return order.orderer()
//                .ordererId()
//                .equals(memberId);
//    }
//
//    private boolean isViewer(TeamMemberRole role) {
//        return role.equals(TeamMemberRole.VIEWER);
//    }
//
//    /**
//     * 팀에 ADMIN 이 있는지 검증
//     *
//     * @param teamId 팀 ID
//     */
//    private boolean hasAdminInTeam(Long teamId) {
//        return teamRoleVerifier.hasAdmin(teamId);
//    }
//
//}
