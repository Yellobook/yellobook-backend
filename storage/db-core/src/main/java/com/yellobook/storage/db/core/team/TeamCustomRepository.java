package com.yellobook.storage.db.core.team;

import com.yellobook.storage.db.core.team.dto.query.QueryTeamMember;
import java.util.List;

public interface TeamCustomRepository {
    List<QueryTeamMember> findTeamMembers(Long teamId);
}
