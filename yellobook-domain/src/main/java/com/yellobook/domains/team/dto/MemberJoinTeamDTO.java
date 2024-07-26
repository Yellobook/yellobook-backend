package com.yellobook.domains.team.dto;

import com.yellobook.enums.MemberTeamRole;
import lombok.Getter;

@Getter
public class MemberJoinTeamDTO {
    private final MemberTeamRole role;
    private final String teamName;

    public MemberJoinTeamDTO(MemberTeamRole role, String teamName){
        this.role = role;
        this.teamName = teamName;
    }
}