package com.yellobook.inform;

import com.yellobook.BaseEntity;
import com.yellobook.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "inform_mentions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_member_inform", columnNames = {"member_id", "inform_id"})
        }
)
public class InformMentionEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inform_id", nullable = false)
    private InformEntity inform;

    protected InformMentionEntity() {
    }

    @Builder
    public InformMentionEntity(InformEntity inform, MemberEntity member) {
        this.inform = inform;
        this.member = member;
    }
}