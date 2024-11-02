package com.yellobook.domains.member.mapper;

import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.dto.response.ProfileResponse.ParticipantInfo;
import com.yellobook.domains.member.dto.response.TermAllowanceResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "memberJoinTeams", target = "teams")
    ProfileResponse toProfileResponse(Member member, List<QueryMemberJoinTeam> memberJoinTeams);

    @Mapping(source = "role.description", target = "role")
    ParticipantInfo toParticipantInfo(QueryMemberJoinTeam memberJoinTeam);

    List<ParticipantInfo> toParticipantInfoList(List<QueryMemberJoinTeam> memberJoinTeams);

    TermAllowanceResponse toAllowanceResponse(Boolean allowance);
}
