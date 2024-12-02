package com.yellobook.domains.team.mapper;

import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.dto.request.CreateTeamRequest;
import com.yellobook.domains.team.dto.response.CreateTeamResponse;
import com.yellobook.domains.team.dto.response.GetTeamResponse;
import com.yellobook.domains.team.dto.response.InvitationCodeResponse;
import com.yellobook.domains.team.dto.response.JoinTeamResponse;
import com.yellobook.domains.team.dto.response.TeamMemberListResponse;
import com.yellobook.domains.team.entity.Team;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

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
