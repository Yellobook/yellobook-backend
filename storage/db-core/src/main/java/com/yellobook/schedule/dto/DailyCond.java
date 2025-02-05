package com.yellobook.domains.schedule.dto;

import com.yellobook.core.domain.team.TeamMemberVO;
import lombok.Builder;

@Builder
public record DailyCond(
        int year,
        int month,
        int day,
        TeamMemberVO teamMember
) {
}
