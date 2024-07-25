package com.yellobook.domains.inform.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inform_comments")
public class InformComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Inform inform;

    @Builder
    private InformComment(String content, Member member, Inform inform) {
        this.content = content;
        this.member = member;
        this.inform = inform;
    }
}
