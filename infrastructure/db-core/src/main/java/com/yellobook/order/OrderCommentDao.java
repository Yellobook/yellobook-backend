package com.yellobook.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.order.OrderComment;
import com.yellobook.core.domain.order.OrderCommentRepository;
import com.yellobook.core.domain.order.dto.CreateOrderCommentCommend;
import com.yellobook.member.MemberEntity;
import com.yellobook.member.MemberJpaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCommentDao implements OrderCommentRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final OrderCommentJpaRepository orderCommentJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Autowired
    public OrderCommentDao(JPAQueryFactory jpaQueryFactory, OrderCommentJpaRepository orderCommentJpaRepository,
                           OrderJpaRepository orderJpaRepository, MemberJpaRepository memberJpaRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.orderCommentJpaRepository = orderCommentJpaRepository;
        this.orderJpaRepository = orderJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Long save(CreateOrderCommentCommend dto) {
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(dto.ordererId());
        OrderEntity orderEntity = orderJpaRepository.getReferenceById(dto.orderId());
        OrderCommentEntity orderCommentEntity = OrderCommentEntity.builder()
                .member(memberEntity)
                .order(orderEntity)
                .content(dto.comment())
                .build();
        return orderCommentJpaRepository.save(orderCommentEntity)
                .getId();
    }

    @Override
    public List<OrderComment> getOrderCommentsByOrderId(Long orderId) {
        QOrderComment orderComment = QOrderComment.orderComment;
        QParticipant participant = QParticipant.participant;

        return jpaQueryFactory.select(Projections.constructor(QueryOrderComment.class,
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

        return null;
    }


}
