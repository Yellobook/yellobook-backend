package com.yellobook.domains.member.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.teamspace.entity.Teamspace;
import com.yellobook.enums.MemberTeamRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "participants",
        uniqueConstraints = {
            @UniqueConstraint(name = "uc_teamspaceId_memberId", columnNames = {"teamspace_id", "member_id"})
        }
)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Participant extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamspace_id", nullable = false)
    private Teamspace teamspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberTeamRole role;
}
