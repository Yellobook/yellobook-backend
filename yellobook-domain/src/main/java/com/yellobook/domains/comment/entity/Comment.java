package com.yellobook.domains.comment.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "comments")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 200)
    private String content;

}
