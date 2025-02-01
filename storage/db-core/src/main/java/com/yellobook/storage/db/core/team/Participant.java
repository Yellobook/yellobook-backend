package com.yellobook.storage.db.core.team;

import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "participants",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_participant", columnNames = {"team_id", "member_id"})
        }
)
public class Participant extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamMemberRole teamMemberRole;

    protected Participant() {
    }

    public Participant(TeamEntity team, MemberEntity member, TeamMemberRole teamMemberRole) {
        this.team = team;
        this.member = member;
        this.teamMemberRole = teamMemberRole;
    }


    public TeamEntity getTeam() {
        return team;
    }

    public MemberEntity getMember() {
        return member;
    }

    public TeamMemberRole getTeamMemberRole() {
        return teamMemberRole;
    }
}
