package com.yellobook.domains.order.dto;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCommentDTO {
    private Long commentId;
    private MemberTeamRole role;
    private String content;
}
