package com.yellobook.domain.order.mapper;

import com.yellobook.domain.order.dto.*;
import com.yellobook.domain.order.dto.GetOrderCommentsResponse.CommentInfo;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.OrderCommentDTO;
import com.yellobook.domains.order.dto.OrderDTO;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.domains.order.entity.OrderMention;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.enums.MemberTeamRole;
import com.yellobook.enums.OrderStatus;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "requestDTO.content", target = "content")
    @Mapping(source = "member", target = "member")
    @Mapping(source = "order", target = "order")
    OrderComment toOrderComment(AddOrderCommentRequest requestDTO, Member member, Order order);

    AddOrderCommentResponse toAddOrderCommentResponse(Long commentId);

    @Mapping(source = "role", target = "role")
    CommentInfo toCommentInfo(OrderCommentDTO orderCommentDTOs);  // TODO

    default Order toOrder(MakeOrderRequest requestDTO, Member member, Team team, Product product){
        return Order.builder()
                .view(0)
                .memo(requestDTO.getMemo())
                .date(requestDTO.getDate())
                .orderStatus(OrderStatus.PENDING_CONFIRM)
                .orderAmount(requestDTO.getOrderAmount())
                .product(product)
                .member(member)
                .team(team)
                .build();
    }

    @Mapping(source = "order", target = "order")
    @Mapping(source = "member", target = "member")
    OrderMention toOrderMention(Order order, Member member);

    MakeOrderResponse toMakeOrderResponse(Long orderId);

    GetOrderResponse toGetOrderResponse(OrderDTO orderDTO);


//    @Mapping(source = "comments", target = "comments")
//    GetOrderCommentsResponse toGetOrderCommentsResponse(List<CommentInfo> comments);

    @Named("getRoleDescription")
    default String getRoleDescription(MemberTeamRole memberTeamRole){
        return memberTeamRole.getDescription();
    }

}
