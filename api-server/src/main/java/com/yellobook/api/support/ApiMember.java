package com.yellobook.api.support;

import com.yellobook.api.support.auth.AppMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.ProfileInfo;
import com.yellobook.core.domain.member.SocialInfo;

public record ApiMember(
        Long memberId,
        SocialInfo socialInfo,
        ProfileInfo profileInfo,
        AppMemberRole appMemberRole
) {
    public Member toMember() {
        return new Member(
                memberId,
                profileInfo,
                socialInfo
        );
    }

}
