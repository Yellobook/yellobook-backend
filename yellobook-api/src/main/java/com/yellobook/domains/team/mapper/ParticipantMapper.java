package com.yellobook.domains.team.mapper;

import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.MentionDTO;
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

    default MentionDTO toMentionDTO(List<Long> participantIds) {
        return new MentionDTO(participantIds);
    }
}
