package com.yellobook.domains.team.entity;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.member.entity.Member;
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
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberTeamRole role;

    @Builder
    public Participant(Team team, Member member, MemberTeamRole role) {
        isValid(team, member, role);
        this.team = team;
        this.member = member;
        this.role = role;
    }

    private void isValid(Team team, Member member, MemberTeamRole role) {
        if (team == null || member == null || role == null) {
            throw new IllegalArgumentException("null값이 존재합니다.");
        }
    }
}
