package com.yellobook.domain.order.mapper;

import com.yellobook.domain.order.dto.AddOrderCommentRequest;
import com.yellobook.domain.order.dto.AddOrderCommentResponse;
import com.yellobook.domain.order.dto.GetOrderCommentsResponse;
import com.yellobook.domain.order.dto.GetOrderCommentsResponse.CommentInfo;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.OrderCommentDTO;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.enums.MemberTeamRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "requestDTO.content", target = "content")
    @Mapping(source = "member", target = "member")
    @Mapping(source = "order", target = "order")
    OrderComment toOrderComment(AddOrderCommentRequest requestDTO, Member member, Order order);

    AddOrderCommentResponse toAddOrderCommentResponse(Long commentId);

    @Mapping(source = "role", target = "role", qualifiedByName = "getRoleDescription")
    CommentInfo toCommentInfo(OrderCommentDTO orderCommentDTOs);

//    @Mapping(source = "comments", target = "comments")
//    GetOrderCommentsResponse toGetOrderCommentsResponse(List<CommentInfo> comments);

    @Named("getRoleDescription")
    default String getRoleDescription(MemberTeamRole memberTeamRole){
        return memberTeamRole.getDescription();
    }
}
