package com.yellobook.core.api.domains.team.mapper;

import com.yellobook.core.domains.member.entity.Member;
import com.yellobook.core.api.domains.team.dto.MentionDTO;
import com.yellobook.core.domains.team.entity.Participant;
import com.yellobook.core.domains.team.entity.Team;
import com.yellobook.core.core.enums.TeamMemberRole;
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
