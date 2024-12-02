package com.yellobook.core.domains.order.entity;

import com.yellobook.core.core.enums.OrderStatus;
import com.yellobook.core.domains.common.entity.BaseEntity;
import com.yellobook.core.domains.inventory.entity.Product;
import com.yellobook.core.domains.member.entity.Member;
import com.yellobook.core.domains.team.entity.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer view;

    @Column(length = 200)
    private String memo;

    @Column(nullable = false)
    private LocalDate date;

    /**
     * 상품 주문 상태 주문확정: CONFIRMED 주문확정대기: PENDING_CONFIRM 주문정정대기: PENDING_MODIFY
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /**
     * 주문 수량
     */
    @Column(nullable = false)
    private Integer orderAmount;

    /**
     * 주문 상품
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    private Order(Integer view, String memo, LocalDate date, OrderStatus orderStatus, Integer orderAmount,
                  Product product, Member member, Team team) {
        // 필수 필드 검증 추가할 것
        // 양방향 연관관계 필요하면 private 으로 빼서만들고 생성자에 추가할것
        this.view = view;
        this.memo = memo;
        this.date = date;
        this.orderStatus = orderStatus;
        this.orderAmount = orderAmount;
        this.product = product;
        this.member = member;
        this.team = team;
    }

    public void requestModifyOrder() {
        this.orderStatus = OrderStatus.PENDING_MODIFY;
    }

    public void confirmOrder() {
        this.orderStatus = OrderStatus.CONFIRMED;
    }

}
