package com.yellobook.api.support.auth;


public record AccessTokenPayload(
        Long memberId,
        AppMemberRole role
) {
}
