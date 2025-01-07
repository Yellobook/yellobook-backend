package com.yellobook.core.domain.team;

import java.util.List;

public interface TeamRepository {
    List<Team> getTeamsByMemberId(Long memberId);
}
