package com.yellobook.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberTeamRole {
    ADMIN("관리자"),
    ORDERER("주문자"),
    VIEWER("뷰어");

    private final String description;
}

