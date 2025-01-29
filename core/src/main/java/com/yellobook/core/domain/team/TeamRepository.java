package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository {
    List<Team> getTeamsByMemberId(Long memberId);

    boolean existByTeamAndMemberAndRole(Long teamId, Long memberId, TeamMemberRole role);

    boolean existByTeamAndRole(Long teamId, TeamMemberRole role);

    boolean existByTeamIdAndMemberId(Long teamId, Long memberId);

    Long save(String name, String phoneNumber, String address, Searchable searchable);

    Optional<Team> findById(Long teamId);

    //List<Member> getMembersByTeamId(Long teamId);

    void join(Long teamId, Member member, TeamMemberRole role);

    void leave(Long teamId, Member member);

    boolean existByName(String name);

    TeamMemberRole getRole(Long teamId, Long memberId);

    List<Team> getPublicTeamsByName(String keyword);

    void updateSearchable(Long teamId, Searchable searchable);

    Long applyTeam(Long teamId, Long memberId);

    boolean hasAppliedTeam(Long teamId, Long memberId);

    Optional<TeamApplyInfo> findTeamApplyById(Long applyId);

    void updateJoinStatus(Long applyId, JoinStatus joinStatus);

    boolean hasRequestedOrdererConversion(Long teamId, Long memberId);

    Long requestOrdererConversion(Long teamId, Long memberId);

    Optional<RoleConversionInfo> findTeamRoleConversionById(Long conversionId);

    void updateRoleConversionStatus(Long requestId, ChangeRoleStatus changeRoleStatus);

    void updateTeamMemberRole(Long teamId, Long memberId, TeamMemberRole role);
}
