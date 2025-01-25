package com.yellobook.team;

import com.yellobook.BaseEntity;
import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "participants",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_participant", columnNames = {"team_id", "member_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamMemberRole teamMemberRole;

    @Builder
    public Participant(TeamEntity team, MemberEntity member, TeamMemberRole teamMemberRole) {
        this.team = team;
        this.member = member;
        this.teamMemberRole = teamMemberRole;
    }
}
