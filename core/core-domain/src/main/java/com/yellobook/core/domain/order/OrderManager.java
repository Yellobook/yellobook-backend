//package com.yellobook.core.domain.order;
//
//import static com.yellobook.core.error.CoreErrorType.ORDER_AMOUNT_EXCEED;
//import static com.yellobook.core.error.CoreErrorType.ORDER_CANT_CANCEL;
//import static com.yellobook.core.error.CoreErrorType.ORDER_CONFIRMED_CANT_MODIFY;
//import static com.yellobook.core.error.CoreErrorType.ORDER_PENDING_MODIFY_CANT_CONFIRM;
//
//import com.yellobook.core.domain.inventory.ProductReader;
//import com.yellobook.core.domain.inventory.ProductWriter;
//import com.yellobook.core.error.CoreException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OrderManager {
//    private final ProductWriter productWriter;
//    private final ProductReader productReader;
//    private final OrderRepository orderRepository;
//
//    @Autowired
//    public OrderManager(ProductWriter productWriter, ProductReader productReader, OrderRepository orderRepository) {
//        this.productWriter = productWriter;
//        this.productReader = productReader;
//        this.orderRepository = orderRepository;
//    }
//
//    // 주문 정정 요청
//    public void requestModify(Order order) {
//        if (order.orderStatus()
//                .equals(OrderStatus.PENDING_CONFIRM)) {
//            throw new CoreException(ORDER_CONFIRMED_CANT_MODIFY);
//        }
//    }
//
//    // 주문 확정 - 주문 정정 요청이 되어 있으면 주문 확정 불가능
//    public void canConfirmOrder(Order order) {
//        // 주문 정정 요청이 되어 있으면 주문 확정 불가능
//        if (order.orderStatus()
//                .equals(OrderStatus.PENDING_MODIFY)) {
//            throw new CoreException(ORDER_PENDING_MODIFY_CANT_CONFIRM);
//        }
//    }
//
//    // 주문 취소 - 주문 정정 상태가 아니면 취소 불가능
//    public void canCancelOrder(Order order) {
//        if (!order.orderStatus()
//                .equals(OrderStatus.PENDING_MODIFY)) {
//            throw new CoreException(ORDER_CANT_CANCEL);
//        }
//    }
//
//    // 주문한 상품의 제고 감소
//    public void reduceProductAmount(Order order) {
//        // 제품 수량 검증
//        isOrderAmountExceedProductAmount(
//                order.orderInfo()
//                        .amount(),
//                order.orderInfo()
//                        .product()
//                        .amount()
//        );
//        // 수량 감소
//        productWriter.updateAmount(
//                order.orderInfo()
//                        .product()
//                        .amount() + order.orderInfo()
//                        .amount(),
//                order.orderInfo()
//                        .product()
//                        .productId());
//    }
//
//    /**
//     * 주문 수량이 제품 수량 보다 많으면 주문 불가능
//     *
//     * @param orderAmount   주문 수량
//     * @param productAmount 제품 수량
//     * @return
//     */
//    public void isOrderAmountExceedProductAmount(Integer orderAmount, Integer productAmount) {
//        if (orderAmount > productAmount) {
//            throw new CoreException(ORDER_AMOUNT_EXCEED);
//        }
//    }
//
//    // 상품의 제고 복구
//    public void restoreProductAmount(Order order) {
//        productWriter.updateAmount(
//                order.orderInfo()
//                        .product()
//                        .amount() - order.orderInfo()
//                        .amount(),
//                order.orderInfo()
//                        .product()
//                        .productId());
//    }
//
//    // 상품 수량 조회
//    public int readProductAmount(Long productId) {
//        return productReader.read(productId)
//                .amount();
//    }
//
//    public void existProduct(Long productId) {
//        productReader.read(productId);
//    }
//
//
//}
