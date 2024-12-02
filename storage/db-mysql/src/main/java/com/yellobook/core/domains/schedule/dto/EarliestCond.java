package com.yellobook.core.domains.schedule.dto;

import com.yellobook.core.common.vo.TeamMemberVO;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record EarliestCond(
        LocalDate today,
        TeamMemberVO teamMember
) {
}
