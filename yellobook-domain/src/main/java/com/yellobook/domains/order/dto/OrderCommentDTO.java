package com.yellobook.domains.order.dto;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCommentDTO {
    private Long commentId;
    private MemberTeamRole role;
    private String content;
}
