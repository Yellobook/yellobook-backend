package com.yellobook.domains.schedule.dto;

import com.yellobook.common.vo.TeamMemberVO;
import lombok.Builder;

@Builder
public record DailyCond(
        int year,
        int month,
        int day,
        TeamMemberVO teamMember
) {
}
