package com.yellobook.domains.team.mapper;

import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.dto.request.CreateTeamRequest;
import com.yellobook.domains.team.dto.response.*;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team toTeam(CreateTeamRequest request);

    @Mapping(source = "id", target = "teamId")
    CreateTeamResponse toCreateTeamResponse(Team team);
    InvitationCodeResponse toInvitationCodeResponse(String inviteUrl);

    @Mapping(source = "id", target = "teamId")
    JoinTeamResponse toJoinTeamResponse(Team team);

    @Mapping(source = "id", target = "teamId")
    GetTeamResponse toGetTeamResponse(Team team);

    default TeamMemberListResponse toTeamMemberListResponse(List<QueryTeamMember> members) {
        return new TeamMemberListResponse(members);
    }
}
