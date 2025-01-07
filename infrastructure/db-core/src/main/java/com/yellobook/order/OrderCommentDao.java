package com.yellobook.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.order.OrderCommentRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCommentDao implements OrderCommentRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final OrderCommentJpaRepository orderCommentJpaRepository;

    public OrderCommentDao(JPAQueryFactory jpaQueryFactory, OrderCommentJpaRepository orderCommentJpaRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.orderCommentJpaRepository = orderCommentJpaRepository;
    }

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
}
