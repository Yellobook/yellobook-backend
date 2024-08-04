package com.yellobook.domain.team.mapper;

import com.yellobook.domains.member.entity.Member;
import com.yellobook.domain.inform.dto.MentionDTO;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.common.enums.MemberTeamRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    @Mapping(source = "team",target = "team")
    @Mapping(source = "member",target = "member")
    @Mapping(source = "memberTeamRole",target = "role")
    @Mapping(target = "id", ignore = true)
    Participant toParticipant(MemberTeamRole memberTeamRole, Team team, Member member);

    @Mapping(source ="member.id", target = "id")
    @Mapping(source ="member.nickname", target = "nickname")
    MentionDTO toMentionDTO(Participant participants);

    List<MentionDTO> toMentionDTOlist(List<Participant> participants);
}
