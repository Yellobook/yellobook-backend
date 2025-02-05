package com.yellobook.storage.db.core.inform;

import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inform_comments")
public class InformCommentEntity extends BaseEntity {
    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inform_id", nullable = false)
    private InformEntity inform;

    protected InformCommentEntity() {
    }

    public InformCommentEntity(String content, MemberEntity member, InformEntity inform) {
        this.content = content;
        this.member = member;
        this.inform = inform;
    }

    public String getContent() {
        return content;
    }

    public MemberEntity getMember() {
        return member;
    }

    public InformEntity getInform() {
        return inform;
    }
}
