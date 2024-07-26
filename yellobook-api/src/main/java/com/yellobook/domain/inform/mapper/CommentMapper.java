package com.yellobook.domain.inform.mapper;

import com.yellobook.domain.inform.dto.InformCommentRequest;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "request.content", target = "content")
    InformComment toComment(InformCommentRequest.PostCommentRequestDTO request, Member member, Inform inform);
}
