package com.yellobook.domain.inform.mapper;

import com.yellobook.domain.inform.dto.InformRequest;
import com.yellobook.domain.inform.dto.InformResponse;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InformMapper {
    @Mapping(source = "request.memo", target = "content")
    Inform toInform(InformRequest.CreateInformRequestDTO request, Member member, Team team);

    @Mapping(source = "inform.id", target = "informId")
    InformResponse.CreateInformResponseDTO toCreateInformResponseDTO(Inform inform);

    @Mapping(target = "inform", source = "inform")
    @Mapping(target = "member", source = "member")
    InformMention toInformMention(Inform inform, Member member);

    @Mapping(target ="title", source = "inform.title")
    @Mapping(target ="memo", source = "inform.content")
    @Mapping(target ="view", source = "inform.view")
    @Mapping(target = "date", source = "inform.date")
    @Mapping(target = "mentioned", source = "mentions")
    @Mapping(target = "comments", source = "comments")
    InformResponse.GetInformResponseDTO toGetInformResponseDTO(Inform inform, List<InformComment> comments, List<InformMention> mentions);

}
