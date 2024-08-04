package com.yellobook.domain.team.mapper;

import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domain.inform.dto.MentionDTO;
import com.yellobook.domains.team.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);


    @Mapping(source ="member.id", target = "id")
    @Mapping(source ="member.nickname", target = "nickname")
    MentionDTO toMentionDTO(Participant participants);

    List<MentionDTO> toMentionDTOlist(List<Participant> participants);
}
