package com.yellobook.core.api.domains.team.dto.response;

import com.yellobook.core.domains.team.dto.query.QueryTeamMember;
import java.util.List;

public record TeamMemberListResponse(
        List<QueryTeamMember> members
) {
}

