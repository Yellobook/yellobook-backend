package com.yellobook.core.domain.inform;

import com.yellobook.core.domain.member.Member;

public record InformComment(
        Long commentId,
        Member commenter
) {
}
