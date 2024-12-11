package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.dto.query.QueryTeamMember;
import java.util.List;

public interface TeamCustomRepository {
    List<QueryTeamMember> findTeamMembers(Long teamId);
}
