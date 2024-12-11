package com.yellobook.domains.schedule.dto;

import com.yellobook.common.vo.TeamMemberVO;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record EarliestCond(
        LocalDate today,
        TeamMemberVO teamMember
) {
}
