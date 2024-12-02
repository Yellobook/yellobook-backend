package com.yellobook.core.domains.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domains.order.dto.query.QueryOrder;
import com.yellobook.core.domains.order.dto.query.QueryOrderComment;
import com.yellobook.core.domains.order.entity.QOrder;
import com.yellobook.core.domains.order.entity.QOrderComment;
import com.yellobook.core.domains.team.entity.QParticipant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<QueryOrderComment> getOrderComments(Long orderId) {
        QOrderComment orderComment = QOrderComment.orderComment;
        QParticipant participant = QParticipant.participant;

        return queryFactory.select(Projections.constructor(QueryOrderComment.class,
                        orderComment.id.as("commentId"),
                        participant.teamMemberRole,
                        orderComment.content,
                        orderComment.createdAt
                ))
                .from(orderComment)
                .join(participant)
                .on(orderComment.member.id.eq(participant.member.id))
                .where(orderComment.order.id.eq(orderId), orderComment.order.team.id.eq(participant.team.id))
                .orderBy(orderComment.createdAt.asc())
                .fetch();
    }

    @Override
    public QueryOrder getOrder(Long orderId) {
        QOrder order = QOrder.order;

        return queryFactory.select(Projections.constructor(QueryOrder.class,
                        order.date,
                        order.member.nickname.as("writer"),
                        order.product.name.as("productName"),
                        order.product.subProduct.as("subProductName"),
                        order.orderAmount.as("amount"),
                        order.memo
                ))
                .from(order)
                .where(order.id.eq(orderId))
                .fetchOne();
    }
}

