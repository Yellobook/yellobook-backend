package com.yellobook.storage.db.core.order;

import com.yellobook.core.domain.order.OrderStatus;
import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.inventory.ProductEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.team.TeamEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @Column(nullable = false)
    private Integer view;

    @Column(length = 200)
    private String memo;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private Integer orderAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    protected OrderEntity() {
    }

    private OrderEntity(Integer view, String memo, LocalDate date, OrderStatus orderStatus, Integer orderAmount,
                        ProductEntity product, MemberEntity member, TeamEntity team) {
        this.view = view;
        this.memo = memo;
        this.date = date;
        this.orderStatus = orderStatus;
        this.orderAmount = orderAmount;
        this.product = product;
        this.member = member;
        this.team = team;
    }

    public Integer getView() {
        return view;
    }

    public String getMemo() {
        return memo;
    }

    public LocalDate getDate() {
        return date;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public MemberEntity getMember() {
        return member;
    }

    public TeamEntity getTeam() {
        return team;
    }
}
