package com.yellobook.domains.order.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_mentions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_member_order", columnNames = {"member_id", "order_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMention extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}