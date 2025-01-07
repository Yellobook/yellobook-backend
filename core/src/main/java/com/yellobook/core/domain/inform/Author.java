package com.yellobook.core.domain.inform;

// VO (애그리거트 루트가 아니니까)
public record Author(
        Long memberId,
        String nickname
) {
}
