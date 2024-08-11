package com.yellobook.domains.inform.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInformMention is a Querydsl query type for InformMention
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInformMention extends EntityPathBase<InformMention> {

    private static final long serialVersionUID = -773310803L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInformMention informMention = new QInformMention("informMention");

    public final com.yellobook.domains.common.entity.QBaseEntity _super = new com.yellobook.domains.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QInform inform;

    public final com.yellobook.domains.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QInformMention(String variable) {
        this(InformMention.class, forVariable(variable), INITS);
    }

    public QInformMention(Path<? extends InformMention> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInformMention(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInformMention(PathMetadata metadata, PathInits inits) {
        this(InformMention.class, metadata, inits);
    }

    public QInformMention(Class<? extends InformMention> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.inform = inits.isInitialized("inform") ? new QInform(forProperty("inform"), inits.get("inform")) : null;
        this.member = inits.isInitialized("member") ? new com.yellobook.domains.member.entity.QMember(forProperty("member")) : null;
    }

}

