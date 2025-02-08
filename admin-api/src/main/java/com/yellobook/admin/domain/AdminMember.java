package com.yellobook.admin.domain;

import com.yellobook.admin.security.AdminMemberRole;

public record AdminMember(
        long adminId,
        String username,
        String password,
        AdminMemberRole role,
        String sub
) {
}