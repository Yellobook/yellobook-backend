package com.yellobook.domains.mentioned.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "mentioned",
        uniqueConstraints = {
            @UniqueConstraint(name = "uc_memberId_post_id", columnNames = {"member_id", "post_id"})
        }
)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Mentioned extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
