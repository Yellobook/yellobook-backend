package com.yellobook.domains.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -1960304175L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final com.yellobook.domains.common.entity.QBaseEntity _super = new com.yellobook.domains.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.yellobook.domains.member.entity.QMember member;

    public final StringPath memo = createString("memo");

    public final NumberPath<Integer> orderAmount = createNumber("orderAmount", Integer.class);

    public final EnumPath<com.yellobook.common.enums.OrderStatus> orderStatus = createEnum("orderStatus", com.yellobook.common.enums.OrderStatus.class);

    public final com.yellobook.domains.inventory.entity.QProduct product;

    public final com.yellobook.domains.team.entity.QTeam team;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> view = createNumber("view", Integer.class);

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.yellobook.domains.member.entity.QMember(forProperty("member")) : null;
        this.product = inits.isInitialized("product") ? new com.yellobook.domains.inventory.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.team = inits.isInitialized("team") ? new com.yellobook.domains.team.entity.QTeam(forProperty("team")) : null;
    }

}

