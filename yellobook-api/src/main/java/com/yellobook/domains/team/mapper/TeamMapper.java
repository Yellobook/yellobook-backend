package com.yellobook.domains.team.mapper;

import com.yellobook.domains.team.dto.TeamRequest;
import com.yellobook.domains.team.dto.TeamResponse;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team toTeam(TeamRequest.CreateTeamRequestDTO request);

    @Mapping(source = "id", target = "teamId")
    TeamResponse.CreateTeamResponseDTO toCreateTeamResponseDTO(Team team);
    TeamResponse.LeaveTeamResponseDTO toLeaveTeamResponseDTO(Long teamId);
    TeamResponse.InvitationCodeResponseDTO toInvitationCodeResponseDTO(String inviteUrl);
    @Mapping(source = "id", target = "teamId")
    TeamResponse.JoinTeamResponseDTO toJoinTeamResponseDTO(Team team);
    @Mapping(source = "id", target = "teamId")
    TeamResponse.GetTeamResponseDTO toGetTeamResponseDTO(Team team);
}
