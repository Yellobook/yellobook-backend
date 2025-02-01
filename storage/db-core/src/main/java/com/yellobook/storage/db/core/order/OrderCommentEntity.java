package com.yellobook.storage.db.core.order;

import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_comments")
public class OrderCommentEntity extends BaseEntity {
    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    protected OrderCommentEntity() {
    }

    public OrderCommentEntity(String content, MemberEntity member, OrderEntity order) {
        this.content = content;
        this.member = member;
        this.order = order;
    }

}