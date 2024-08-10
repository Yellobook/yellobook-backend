package com.yellobook.domains.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderComment is a Querydsl query type for OrderComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderComment extends EntityPathBase<OrderComment> {

    private static final long serialVersionUID = -1990924466L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderComment orderComment = new QOrderComment("orderComment");

    public final com.yellobook.domains.common.entity.QBaseEntity _super = new com.yellobook.domains.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.yellobook.domains.member.entity.QMember member;

    public final QOrder order;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QOrderComment(String variable) {
        this(OrderComment.class, forVariable(variable), INITS);
    }

    public QOrderComment(Path<? extends OrderComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderComment(PathMetadata metadata, PathInits inits) {
        this(OrderComment.class, metadata, inits);
    }

    public QOrderComment(Class<? extends OrderComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.yellobook.domains.member.entity.QMember(forProperty("member")) : null;
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

