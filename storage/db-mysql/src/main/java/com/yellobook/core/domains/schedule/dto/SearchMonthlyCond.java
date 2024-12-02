package com.yellobook.core.domains.schedule.dto;

import com.yellobook.core.common.vo.TeamMemberVO;
import lombok.Builder;

@Builder
public record SearchMonthlyCond(
        String keyword,
        int year,
        int month,
        TeamMemberVO teamMember
) {
}
