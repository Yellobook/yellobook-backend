package com.yellobook.order;

import com.yellobook.BaseEntity;
import com.yellobook.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "order_mentions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_member_order", columnNames = {"member_id", "order_id"})
        }
)
public class OrderMentionEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    protected OrderMentionEntity() {
    }

    @Builder
    private OrderMentionEntity(MemberEntity member, OrderEntity order) {
        this.member = member;
        this.order = order;
    }
}