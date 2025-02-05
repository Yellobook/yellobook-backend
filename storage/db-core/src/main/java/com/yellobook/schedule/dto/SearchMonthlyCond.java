package com.yellobook.domains.schedule.dto;

import com.yellobook.core.domain.team.TeamMemberVO;
import lombok.Builder;

@Builder
public record SearchMonthlyCond(
        String keyword,
        int year,
        int month,
        TeamMemberVO teamMember
) {
}
