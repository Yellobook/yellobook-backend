package com.yellobook.domains.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.order.dto.OrderCommentDTO;
import com.yellobook.domains.order.dto.OrderDTO;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.QOrder;
import com.yellobook.domains.order.entity.QOrderComment;
import com.yellobook.domains.team.entity.QParticipant;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public OrderRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<OrderCommentDTO> getOrderComments(Long orderId) {
        QOrderComment orderComment = QOrderComment.orderComment;
        QParticipant participant = QParticipant.participant;

        return queryFactory.select(Projections.constructor(OrderCommentDTO.class,
                    orderComment.id.as("commentId"),
                    participant.role,
                    orderComment.content
                ))
                .from(orderComment)
                .join(participant).on(orderComment.member.id.eq(participant.member.id))
                .where(orderComment.id.eq(orderId))
                .orderBy(orderComment.createdAt.asc())
                .fetch();
    }

    @Override
    public OrderDTO getOrder(Long orderId) {
        QOrder order = QOrder.order;

        return queryFactory.select(Projections.constructor(OrderDTO.class,
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

