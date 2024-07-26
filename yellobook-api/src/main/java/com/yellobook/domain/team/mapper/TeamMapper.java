package com.yellobook.domain.team.mapper;

import com.yellobook.domain.team.dto.TeamRequest;
import com.yellobook.domain.team.dto.TeamResponse;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "id", ignore = true)
    Team toTeam(TeamRequest.CreateTeamRequestDTO request);
    TeamResponse.CreateTeamResponseDTO toCreateTeamResponseDTO(Team team);

}
