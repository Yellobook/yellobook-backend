package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository {
    List<Team> getTeamsByMemberId(Long memberId);

    boolean existByTeamAndMemberAndRole(Long teamId, Long memberId, TeamMemberRole role);  // participant jpa repo

    boolean existByTeamAndRole(Long teamId, TeamMemberRole role); // participant jpa repo

    boolean existByTeamAndMemberId(Long teamId, Member member);

    Long save(String name, String phoneNumber, String address);

    Optional<Team> findById(Long teamId);

    //List<Member> getMembersByTeamId(Long teamId);

    void join(Long teamId, Member member, TeamMemberRole role);

    void leave(Long teamId, Member member);

    boolean existByName(String name);

    TeamMemberRole getRole(Long teamId, Long memberId);
}
