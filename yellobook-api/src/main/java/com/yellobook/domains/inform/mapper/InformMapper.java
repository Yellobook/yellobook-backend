package com.yellobook.domains.inform.mapper;

import com.yellobook.domains.inform.dto.request.CreateInformRequest;
import com.yellobook.domains.inform.dto.response.CreateInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse.CommentItem;
import com.yellobook.domains.inform.dto.response.GetInformResponse.MentionItem;
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
    Inform toInform(CreateInformRequest request, Member member, Team team);

    @Mapping(source = "inform.id", target = "informId")
    CreateInformResponse toCreateInformResponse(Inform inform);

    @Mapping(target = "inform", source = "inform")
    @Mapping(target = "member", source = "member")
    InformMention toInformMention(Inform inform, Member member);

    @Mapping(target = "title", source = "inform.title")
    @Mapping(target = "memo", source = "inform.content")
    @Mapping(target = "view", source = "inform.view")
    @Mapping(target = "date", source = "inform.date")
    GetInformResponse toGetInformResponseDTO(Inform inform, List<InformComment> comments, List<Member> members);

    default List<CommentItem> mapComments(List<InformComment> comments) {
        return comments.stream()
                .map(comment -> CommentItem.builder()
                        .id(comment.getId())
                        .memberId(comment.getMember().getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();
    }

    default List<MentionItem> mapMentions(List<Member> members) {
        return members.stream()
                .map(member -> MentionItem.builder()
                        .memberId(member.getId())
                        .memberNickname(member.getNickname()) // Assuming "nickname" is correct
                        .build())
                .toList();
    }
}
