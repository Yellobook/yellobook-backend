package com.yellobook.domain.team.mapper;

import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    Team toTeam(TeamRequest.CreateTeamRequestDTO request);

    @Mapping(source = "id", target = "teamId")
    TeamResponse.CreateTeamResponseDTO toCreateTeamResponseDTO(Team team);
    TeamResponse.LeaveTeamResponseDTO toLeaveTeamResponseDTO(Long teamId);
    TeamResponse.InvitationCodeResponseDTO toInvitationCodeResponseDTO(String inviteUrl);
    TeamResponse.JoinTeamResponseDTO toJoinTeamResponseDTO(Team team);
    @Mapping(source = "id", target = "teamId")
    TeamResponse.GetTeamResponseDTO toGetTeamResponseDTO(Team team);
}
