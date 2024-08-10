package com.yellobook.domains.schedule.dto;

import com.yellobook.common.vo.TeamMemberVO;
import lombok.Builder;

@Builder
public record MonthlyCond (
        int year,
        int month,
        TeamMemberVO teamMember
) {
}
