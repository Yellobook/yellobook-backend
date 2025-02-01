package com.yellobook.storage.db.core.order;

import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "order_mentions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_member_order", columnNames = {"member_id", "order_id"})
        }
)
public class OrderMentionEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    protected OrderMentionEntity() {
    }

    public OrderMentionEntity(MemberEntity member, OrderEntity order) {
        this.member = member;
        this.order = order;
    }
}