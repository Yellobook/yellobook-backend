package fixture;

import com.yellobook.OrderStatus;
import com.yellobook.inventory.ProductEntity;
import com.yellobook.member.Member;
import com.yellobook.order.OrderCommentEntity;
import com.yellobook.order.OrderEntity;
import com.yellobook.order.OrderMention;
import com.yellobook.team.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import support.ReflectionUtil;

public class OrderFixture {
    private static final int ORDER_VIEW = 1;
    private static final String ORDER_MEMO = "메모";
    private static final OrderStatus ORDER_STATUS = OrderStatus.CONFIRMED;
    private static final int ORDER_AMOUNT = 1;
    private static final LocalDate ORDER_DATE = LocalDate.now();
    private static final LocalDateTime ORDER_TIMESTAMP = LocalDateTime.now();
    private static final String ORDERCOMMENT_CONTENT = "댓글 내용";

    public static OrderEntity createOrder(Team team, Member member, ProductEntity product, LocalDate orderDate) {
        return createOrder(team, member, product, orderDate, ORDER_VIEW, ORDER_MEMO, ORDER_STATUS, ORDER_AMOUNT,
                ORDER_TIMESTAMP);
    }

    public static OrderEntity createOrder(Team team, Member member, ProductEntity product, LocalDate orderDate,
                                          int view,
                                          String memo, OrderStatus status, int amount, LocalDateTime timestamp) {
        OrderEntity order = OrderEntity.builder()
                .view(view)
                .memo(memo)
                .date(orderDate)
                .orderStatus(status)
                .orderAmount(amount)
                .team(team)
                .member(member)
                .product(product)
                .build();
        ReflectionUtil.setBaseTimeEntityFields(order, timestamp);
        return order;
    }

    public static OrderEntity createOrder(Team team, Member member, ProductEntity product, OrderStatus orderStatus) {
        return createOrder(team, member, product, ORDER_DATE, ORDER_VIEW, ORDER_MEMO, orderStatus, ORDER_AMOUNT,
                ORDER_TIMESTAMP);
    }

    public static OrderEntity createOrder(Team team, Member member, ProductEntity product, OrderStatus orderStatus,
                                          Integer amount) {
        return createOrder(team, member, product, ORDER_DATE, ORDER_VIEW, ORDER_MEMO, orderStatus, amount,
                ORDER_TIMESTAMP);
    }

    // 주문 언급
    public static OrderMention createOrderMention(OrderEntity order, Member member) {
        return OrderMention.builder()
                .order(order)
                .member(member)
                .build();
    }

    // 주문 댓글
    public static OrderCommentEntity createOrderComment(OrderEntity order, Member member) {
        return OrderCommentEntity.builder()
                .order(order)
                .member(member)
                .content(ORDERCOMMENT_CONTENT)
                .build();
    }

}

