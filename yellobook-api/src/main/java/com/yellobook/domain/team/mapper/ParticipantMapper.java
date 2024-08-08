package com.yellobook.domain.team.mapper;

import com.yellobook.domains.member.entity.Member;
import com.yellobook.domain.inform.dto.MentionDTO;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.common.enums.MemberTeamRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source = "role", target = "role")
    Participant toParticipant(MemberTeamRole role, Team team, Member member);

    @Mapping(source ="participant.id", target = "id")
    @Mapping(source ="participant.member.nickname", target = "nickname")
    MentionDTO toMentionDTO(Participant participant);

    List<MentionDTO> toMentionDTOlist(List<Participant> participants);
}
