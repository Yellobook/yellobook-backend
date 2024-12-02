package com.yellobook.core.api.domains.member.mapper;

import com.yellobook.core.api.domains.member.dto.response.CurrentTeamResponse;
import com.yellobook.core.api.domains.member.dto.response.ProfileResponse;
import com.yellobook.core.api.domains.member.dto.response.ProfileResponse.ParticipantInfo;
import com.yellobook.core.api.domains.member.dto.response.TermAllowanceResponse;
import com.yellobook.core.domains.member.entity.Member;
import com.yellobook.core.domains.team.dto.query.QueryMemberJoinTeam;
import com.yellobook.core.domains.team.entity.Team;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "participantInfos", target = "teams")
    ProfileResponse toProfileResponseDTO(Member member, List<ParticipantInfo> participantInfos);

    TermAllowanceResponse toAllowanceResponseDTO(Boolean allowance);

    default ParticipantInfo toParticipantInfo(QueryMemberJoinTeam queryMemberJoinTeam) {
        return ParticipantInfo.builder()
                .teamName(queryMemberJoinTeam.teamName())
                .teamId(queryMemberJoinTeam.teamId())
                .role(queryMemberJoinTeam.role()
                        .getDescription())
                .build();
    }

    @Mapping(source = "id", target = "teamId")
    @Mapping(source = "name", target = "teamName")
    CurrentTeamResponse toCurrentTeamResponse(Team team);
}
