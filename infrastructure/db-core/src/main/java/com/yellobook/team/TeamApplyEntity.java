package com.yellobook.team;

import com.yellobook.BaseEntity;
import com.yellobook.core.domain.team.JoinStatus;
import com.yellobook.core.domain.team.TeamApplyInfo;
import com.yellobook.member.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TeamApplyEntity extends BaseEntity {
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
    private JoinStatus status;

    protected TeamApplyEntity() {
    }

    public TeamApplyEntity(TeamEntity team, MemberEntity member, JoinStatus status) {
        this.team = team;
        this.member = member;
        this.status = status;
    }

    public TeamApplyInfo toTeamApplyInfo() {
        return new TeamApplyInfo(
                id,
                team.getId(),
                member.getId(),
                status
        );
    }

}
