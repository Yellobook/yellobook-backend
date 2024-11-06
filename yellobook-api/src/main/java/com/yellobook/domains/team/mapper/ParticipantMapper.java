package com.yellobook.domains.team.mapper;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.MentionDTO;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source = "teamMemberRole", target = "teamMemberRole")
    Participant toParticipant(TeamMemberRole teamMemberRole, Team team, Member member);

    default MentionDTO toMentionDTO(List<Long> participantIds) {
        return new MentionDTO(participantIds);
    }
}
