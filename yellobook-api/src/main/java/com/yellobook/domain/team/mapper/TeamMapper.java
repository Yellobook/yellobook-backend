package com.yellobook.domain.team.mapper;

import com.yellobook.domain.team.dto.request.TeamCreateRequest;
import com.yellobook.domain.team.dto.response.*;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team toTeam(TeamCreateRequest request);

    @Mapping(source = "id", target = "teamId")
    TeamCreateResponse toTeamCreateResponse(Team team);
    TeamLeaveResponse toTeamLeaveResponse(Long teamId);
    InvitationCodeResponse toInvitationCodeResponse(String inviteUrl);
    @Mapping(source = "id", target = "teamId")
    TeamJoinResponse toTeamJoinResponse(Team team);
    @Mapping(source = "id", target = "teamId")
    TeamGetResponse toTeamGetResponse(Team team);
}
