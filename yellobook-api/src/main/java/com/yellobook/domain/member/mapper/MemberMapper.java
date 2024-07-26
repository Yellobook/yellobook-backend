package com.yellobook.domain.member.mapper;

import com.yellobook.domain.member.dto.MemberResponse;
import com.yellobook.domains.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source= "participantInfos", target = "teams")
    MemberResponse.ProfileResponseDTO toProfileResponseDTO(Member member, List<MemberResponse.ParticipantInfo> participantInfos);
}
