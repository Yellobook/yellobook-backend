package com.yellobook.storage.db.core.inform;

import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "inform_mentions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_member_inform", columnNames = {"member_id", "inform_id"})
        }
)
public class InformMentionEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inform_id", nullable = false)
    private InformEntity inform;

    protected InformMentionEntity() {
    }

    public MemberEntity getMember() {
        return member;
    }

    public InformEntity getInform() {
        return inform;
    }
}