package com.yellobook.domains.schedule.dto;

import com.yellobook.common.vo.TeamMemberVO;
import lombok.Builder;

@Builder
public record SearchMonthlyCond(
        String keyword,
        int year,
        int month,
        TeamMemberVO teamMember
) {
}
