package com.yellobook.domains.team.repository;

import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
    Optional<Team> findByName(String name);
    List<QueryTeamMember> findTeamMembers(Long teamId);
}
