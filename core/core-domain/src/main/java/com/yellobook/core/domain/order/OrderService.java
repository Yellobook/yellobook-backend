package com.yellobook.core.domain.order;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.support.pagination.CursorPageResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {
    // TODO
    public Long createOrder(Member member, NewOrder newOrder) {
//        orderPermission.onlyOrdererCanOrder(role);
//        // 관리자 없으면 주문자 주문 불가능
//        orderPermission.cannotOrderWithoutAdmin(teamId);
//        orderManager.existProduct(dto.productId());
//        int productAmount = orderManager.readProductAmount(dto.productId());
//        // 수량 비교
//        orderManager.isOrderAmountExceedProductAmount(dto.orderAmount(), productAmount);
//        return orderWriter.create(dto, memberId, teamId);

        return null;
    }

    // TODO
    public void approveOrder(Member member, StoreOrder storeOrder) {
    }

    // TODO
    public void rejectOrder(Member member, StoreOrder storeOrder) {
    }

    // TODO
    public void cancelOrder(Member member, StoreOrder storeOrder) {
    }

    // TODO
    public Order getOrderDetail(Member member, StoreOrder storeOrder) {
        return null;
    }

    // TODO
    public CursorPageResult<OrdersResult> getOrders(Member member, OrdersCriteria criteria) {
        return null;
    }

}
