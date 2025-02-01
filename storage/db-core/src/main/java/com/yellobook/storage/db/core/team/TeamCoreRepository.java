package com.yellobook.storage.db.core.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.Team;
import com.yellobook.core.domain.team.TeamRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TeamCoreRepository implements TeamRepository {
    @Override
    public List<Team> getTeamsByMemberId(Long memberId) {
        return List.of();
    }

    @Override
    public boolean existByTeamAndMemberAndRole(Long teamId, Long memberId, TeamMemberRole role) {
        return false;
    }

    @Override
    public boolean existByTeamAndRole(Long teamId, TeamMemberRole role) {
        return false;
    }

    @Override
    public boolean existByTeamAndMemberId(Long teamId, Member member) {
        return false;
    }

    @Override
    public Long save(String name, String phoneNumber, String address) {
        return 1L;
    }

    @Override
    public Optional<Team> findById(Long teamId) {
        return Optional.empty();
    }

    @Override
    public void join(Long teamId, Member member, TeamMemberRole role) {

    }

    @Override
    public void leave(Long teamId, Member member) {

    }

    @Override
    public boolean existByName(String name) {
        return false;
    }

    @Override
    public TeamMemberRole getRole(Long teamId, Long memberId) {
        return null;
    }
}
