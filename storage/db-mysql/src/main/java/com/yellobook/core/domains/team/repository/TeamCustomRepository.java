package com.yellobook.core.domains.team.repository;

import com.yellobook.core.domains.team.dto.query.QueryTeamMember;
import java.util.List;

public interface TeamCustomRepository {
    List<QueryTeamMember> findTeamMembers(Long teamId);
}
