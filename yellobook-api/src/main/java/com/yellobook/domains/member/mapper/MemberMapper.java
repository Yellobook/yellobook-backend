package com.yellobook.domains.member.mapper;

import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.dto.response.ProfileResponse.ParticipantInfo;
import com.yellobook.domains.member.dto.response.TermAllowanceResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
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
                .role(queryMemberJoinTeam.role()
                        .getDescription())
                .build();
    }


}
