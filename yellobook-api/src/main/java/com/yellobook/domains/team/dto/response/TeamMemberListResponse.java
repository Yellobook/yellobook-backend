package com.yellobook.domains.team.dto.response;

import com.yellobook.domains.team.dto.query.QueryTeamMember;

import java.util.List;

public record TeamMemberListResponse(
        List<QueryTeamMember> members
) {
}

