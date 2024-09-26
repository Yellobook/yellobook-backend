package fixture;

import com.yellobook.common.enums.OrderStatus;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.domains.order.entity.OrderMention;
import com.yellobook.domains.team.entity.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import support.ReflectionUtils;

public class OrderFixture {
    private static final int ORDER_VIEW = 1;
    private static final String ORDER_MEMO = "메모";
    private static final OrderStatus ORDER_STATUS = OrderStatus.CONFIRMED;
    private static final int ORDER_AMOUNT = 1;
    private static final LocalDate ORDER_DATE = LocalDate.now();
    private static final LocalDateTime ORDER_TIMESTAMP = LocalDateTime.now();
    private static final String ORDERCOMMENT_CONTENT = "댓글 내용";

    public static Order createOrder(Team team, Member member, Product product, LocalDate orderDate) {
        return createOrder(team, member, product, orderDate, ORDER_VIEW, ORDER_MEMO, ORDER_STATUS, ORDER_AMOUNT,
                ORDER_TIMESTAMP);
    }

    public static Order createOrder(Team team, Member member, Product product, LocalDate orderDate, int view,
                                    String memo, OrderStatus status, int amount, LocalDateTime timestamp) {
        Order order = Order.builder()
                .view(view)
                .memo(memo)
                .date(orderDate)
                .orderStatus(status)
                .orderAmount(amount)
                .team(team)
                .member(member)
                .product(product)
                .build();
        ReflectionUtils.setBaseTimeEntityFields(order, timestamp);
        return order;
    }

    public static Order createOrder(Team team, Member member, Product product, OrderStatus orderStatus) {
        return createOrder(team, member, product, ORDER_DATE, ORDER_VIEW, ORDER_MEMO, orderStatus, ORDER_AMOUNT,
                ORDER_TIMESTAMP);
    }

    public static Order createOrder(Team team, Member member, Product product, OrderStatus orderStatus,
                                    Integer amount) {
        return createOrder(team, member, product, ORDER_DATE, ORDER_VIEW, ORDER_MEMO, orderStatus, amount,
                ORDER_TIMESTAMP);
    }

    // 주문 언급
    public static OrderMention createOrderMention(Order order, Member member) {
        return OrderMention.builder()
                .order(order)
                .member(member)
                .build();
    }

    // 주문 댓글
    public static OrderComment createOrderComment(Order order, Member member) {
        return OrderComment.builder()
                .order(order)
                .member(member)
                .content(ORDERCOMMENT_CONTENT)
                .build();
    }

}

