package com.yellobook.domains.inform.mapper;

import com.yellobook.domains.inform.dto.request.CreateInformRequest;
import com.yellobook.domains.inform.dto.response.CreateInformCommentResponse;
import com.yellobook.domains.inform.dto.response.CreateInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse.CommentItem;
import com.yellobook.domains.inform.dto.response.GetInformResponse.MentionItem;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InformMapper {
    @Mapping(source = "request.memo", target = "content")
    Inform toInform(CreateInformRequest request, Member member, Team team);

    @Mapping(source = "id", target = "informId")
    CreateInformResponse toCreateInformResponse(Inform inform);

    @Mapping(source = "inform", target = "inform")
    @Mapping(source = "member", target = "member")
    InformMention toInformMention(Inform inform, Member member);

    @Mapping(source = "inform.content", target = "memo")
    @Mapping(source = "inform.member.nickname", target = "author")
    @Mapping(source = "informComments", target = "comments")
    @Mapping(source = "members", target = "mentions")
    GetInformResponse toGetInformResponse(Inform inform, List<InformComment> informComments, List<Member> members);

    @Mapping(source = "id", target = "memberId")
    CommentItem toCommentItem(InformComment informComment);

    List<CommentItem> toCommentItemList(List<InformComment> comments);

    @Mapping(source = "id", target = "memberId")
    @Mapping(source = "nickname", target = "memberNickname")
    MentionItem toMentionItem(Member member);

    List<MentionItem> toMentionItemList(List<Member> members);

    CreateInformCommentResponse toCreateInformCommentResponse(InformComment comment);
}
