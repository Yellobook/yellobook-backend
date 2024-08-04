package com.yellobook.common.vo;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TeamMemberVO {
    private final Long memberId;
    private final Long teamId;
    private final MemberTeamRole role;

    private TeamMemberVO(Long memberId, Long teamId, MemberTeamRole role) {
        if (memberId == null || teamId == null || role == null) {
            throw new IllegalArgumentException("VO 에 null 값이 존재할 수 없습니다.");
        }
        this.memberId = memberId;
        this.teamId = teamId;
        this.role = role;
    }

    public static TeamMemberVO of(Long memberId, Long teamId, MemberTeamRole role) {
        return new TeamMemberVO(memberId, teamId, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // null 및 클래스타입
        if (o == null || getClass() != o.getClass()) return false;
        TeamMemberVO teamUser = (TeamMemberVO) o;
        return memberId.equals(teamUser.memberId)
                && teamId.equals(teamUser.teamId)
                && role.equals(teamUser.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, teamId, role);
    }
}
