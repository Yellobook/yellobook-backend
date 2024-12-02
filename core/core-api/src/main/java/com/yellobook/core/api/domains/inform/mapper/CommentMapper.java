package com.yellobook.core.api.domains.inform.mapper;


import com.yellobook.core.api.domains.inform.dto.request.CreateInformCommentRequest;
import com.yellobook.core.api.domains.inform.dto.response.CreateInformCommentResponse;
import com.yellobook.core.domains.inform.entity.Inform;
import com.yellobook.core.domains.inform.entity.InformComment;
import com.yellobook.core.domains.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "request.content", target = "content")
    InformComment toComment(CreateInformCommentRequest request, Member member, Inform inform);

    @Mapping(source = "comment.id", target = "id")
    CreateInformCommentResponse toCreateInformCommentResponse(InformComment comment);
}
