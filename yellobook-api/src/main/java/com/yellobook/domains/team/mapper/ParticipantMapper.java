package com.yellobook.domains.team.mapper;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source = "member", target = "member")
    @Mapping(source = "team", target = "team")
    @Mapping(source = "teamMemberRole", target = "teamMemberRole")
    Participant toParticipant(TeamMemberRole teamMemberRole, Team team, Member member);
}
