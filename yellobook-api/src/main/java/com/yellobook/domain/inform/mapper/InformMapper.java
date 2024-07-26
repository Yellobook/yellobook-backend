package com.yellobook.domain.inform.mapper;

import com.yellobook.domain.inform.dto.InformRequest;
import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InformMapper {
    @Mapping(source = "request.memo", target = "content")
    Inform toInform(InformRequest.CreateInformRequestDTO request, Member member, Team team);

    InformResponse.CreateInformResponseDTO toCreateInformResponseDTO(Inform inform);


}
