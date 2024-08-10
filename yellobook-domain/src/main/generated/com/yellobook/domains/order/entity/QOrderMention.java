package com.yellobook.domains.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderMention is a Querydsl query type for OrderMention
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderMention extends EntityPathBase<OrderMention> {

    private static final long serialVersionUID = -1990977831L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderMention orderMention = new QOrderMention("orderMention");

    public final com.yellobook.domains.common.entity.QBaseEntity _super = new com.yellobook.domains.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.yellobook.domains.member.entity.QMember member;

    public final QOrder order;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QOrderMention(String variable) {
        this(OrderMention.class, forVariable(variable), INITS);
    }

    public QOrderMention(Path<? extends OrderMention> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderMention(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderMention(PathMetadata metadata, PathInits inits) {
        this(OrderMention.class, metadata, inits);
    }

    public QOrderMention(Class<? extends OrderMention> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.yellobook.domains.member.entity.QMember(forProperty("member")) : null;
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

