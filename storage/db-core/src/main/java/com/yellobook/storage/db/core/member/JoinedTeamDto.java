package com.yellobook.storage.db.core.member;

import com.querydsl.core.annotations.QueryProjection;

public class JoinedTeamDto {
    private final Long teamId;
    private final String teamName;
    private final String teamDescription;
    private final String myRole;
    private final String sellerName;
    private final int memberCount;

    @QueryProjection
    public JoinedTeamDto(Long teamId, String teamName, String teamDescription, String myRole, String sellerName,
                         int memberCount) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.myRole = myRole;
        this.sellerName = sellerName;
        this.memberCount = memberCount;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public String getMyRole() {
        return myRole;
    }

    public String getSellerName() {
        return sellerName;
    }

    public int getMemberCount() {
        return memberCount;
    }
}
