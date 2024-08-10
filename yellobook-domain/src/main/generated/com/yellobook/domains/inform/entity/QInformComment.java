package com.yellobook.domains.inform.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInformComment is a Querydsl query type for InformComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInformComment extends EntityPathBase<InformComment> {

    private static final long serialVersionUID = -773257438L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInformComment informComment = new QInformComment("informComment");

    public final com.yellobook.domains.common.entity.QBaseEntity _super = new com.yellobook.domains.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QInform inform;

    public final com.yellobook.domains.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QInformComment(String variable) {
        this(InformComment.class, forVariable(variable), INITS);
    }

    public QInformComment(Path<? extends InformComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInformComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInformComment(PathMetadata metadata, PathInits inits) {
        this(InformComment.class, metadata, inits);
    }

    public QInformComment(Class<? extends InformComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.inform = inits.isInitialized("inform") ? new QInform(forProperty("inform"), inits.get("inform")) : null;
        this.member = inits.isInitialized("member") ? new com.yellobook.domains.member.entity.QMember(forProperty("member")) : null;
    }

}

