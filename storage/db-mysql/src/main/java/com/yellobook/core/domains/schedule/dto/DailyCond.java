package com.yellobook.core.domains.schedule.dto;

import com.yellobook.core.common.vo.TeamMemberVO;
import lombok.Builder;

@Builder
public record DailyCond(
        int year,
        int month,
        int day,
        TeamMemberVO teamMember
) {
}
