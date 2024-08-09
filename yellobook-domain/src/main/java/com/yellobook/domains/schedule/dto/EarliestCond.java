package com.yellobook.domains.schedule.dto;

import com.yellobook.common.vo.TeamMemberVO;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EarliestCond(
        LocalDate today,
        TeamMemberVO teamMember
) {
}
