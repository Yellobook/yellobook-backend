package com.yellobook.team;

import com.yellobook.BaseEntity;
import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.team.ChangeRoleStatus;
import com.yellobook.core.domain.team.RoleConversionInfo;
import com.yellobook.member.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TeamRoleChangeEntity extends BaseEntity {
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
    private TeamMemberRole requestRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeRoleStatus status;

    protected TeamRoleChangeEntity() {
    }

    public TeamRoleChangeEntity(TeamEntity team, MemberEntity member, TeamMemberRole requestRole, ChangeRoleStatus status) {
        this.team = team;
        this.member = member;
        this.requestRole = requestRole;
        this.status = status;
    }

    public RoleConversionInfo toRoleConversionInfo() {
        return new RoleConversionInfo(
                id,
                team.getId(),
                member.getId(),
                requestRole,
                status
        );
    }

}
