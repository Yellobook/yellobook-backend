package com.yellobook.domains.inform.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "inform_mentions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_member_inform", columnNames = {"member_id", "inform_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InformMention extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inform_id", nullable = false)
    private Inform inform;

    @Builder
    public InformMention(Inform inform, Member member) {
        this.inform = inform;
        this.member = member;
    }
}