package com.yellobook.domains.inform.mapper;

import com.yellobook.domains.inform.dto.InformRequest;
import com.yellobook.domains.inform.dto.InformResponse;
import com.yellobook.domains.inform.dto.MentionDTO;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface InformMapper {
    @Mapping(source = "request.memo", target = "content")
    Inform toInform(InformRequest.CreateInformRequestDTO request, Member member, Team team);

    @Mapping(source = "inform.id", target = "informId")
    InformResponse.CreateInformResponseDTO toCreateInformResponseDTO(Inform inform);

    @Mapping(target = "inform", source = "inform")
    @Mapping(target = "member", source = "member")
    InformMention toInformMention(Inform inform, Member member);

    @Mapping(target = "title", source = "inform.title")
    @Mapping(target = "memo", source = "inform.content")
    @Mapping(target = "view", source = "inform.view")
    @Mapping(target = "date", source = "inform.date")
    @Mapping(target = "mentioned", source = "mentions")
    @Mapping(target = "comments", source = "comments")
    InformResponse.GetInformResponseDTO toGetInformResponseDTO(Inform inform, List<InformComment> comments, List<InformMention> mentions);

    default MentionDTO map(List<InformMention> mentions) {
        if (mentions == null || mentions.isEmpty()) {
            return new MentionDTO(List.of());
        }

        List<Long> ids = mentions.stream()
                .map(InformMention::getId)
                .collect(Collectors.toList());

        return new MentionDTO(ids);
    }
}
