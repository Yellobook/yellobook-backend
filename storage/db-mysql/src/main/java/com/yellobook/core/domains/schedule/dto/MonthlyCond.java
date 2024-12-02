package com.yellobook.core.domains.schedule.dto;

import com.yellobook.core.common.vo.TeamMemberVO;
import lombok.Builder;

@Builder
public record MonthlyCond(
        int year,
        int month,
        TeamMemberVO teamMember
) {
}
