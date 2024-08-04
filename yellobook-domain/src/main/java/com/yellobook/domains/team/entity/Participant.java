package com.yellobook.domains.team.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.common.enums.MemberTeamRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "participants",
        uniqueConstraints = {
            @UniqueConstraint(name = "uc_participant", columnNames = {"team_id", "member_id"})
        }
)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Participant extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberTeamRole role;
}
