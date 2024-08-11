package com.yellobook.domains.inform.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInform is a Querydsl query type for Inform
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInform extends EntityPathBase<Inform> {

    private static final long serialVersionUID = -876386179L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInform inform = new QInform("inform");

    public final com.yellobook.domains.common.entity.QBaseEntity _super = new com.yellobook.domains.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.yellobook.domains.member.entity.QMember member;

    public final com.yellobook.domains.team.entity.QTeam team;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> view = createNumber("view", Integer.class);

    public QInform(String variable) {
        this(Inform.class, forVariable(variable), INITS);
    }

    public QInform(Path<? extends Inform> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInform(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInform(PathMetadata metadata, PathInits inits) {
        this(Inform.class, metadata, inits);
    }

    public QInform(Class<? extends Inform> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.yellobook.domains.member.entity.QMember(forProperty("member")) : null;
        this.team = inits.isInitialized("team") ? new com.yellobook.domains.team.entity.QTeam(forProperty("team")) : null;
    }

}

