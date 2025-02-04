package com.yellobook.domains.order.mapper;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.common.enums.OrderStatus;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.dto.request.AddOrderCommentRequest;
import com.yellobook.domains.order.dto.request.MakeOrderRequest;
import com.yellobook.domains.order.dto.response.AddOrderCommentResponse;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse.CommentInfo;
import com.yellobook.domains.order.dto.response.GetOrderResponse;
import com.yellobook.domains.order.dto.response.MakeOrderResponse;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.domains.order.entity.OrderMention;
import com.yellobook.domains.team.entity.Team;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "requestDTO.content", target = "content")
    @Mapping(source = "member", target = "member")
    @Mapping(source = "order", target = "order")
    OrderComment toOrderComment(AddOrderCommentRequest requestDTO, Member member, Order order);

    AddOrderCommentResponse toAddOrderCommentResponse(Long commentId);

    @Mapping(source = "role", target = "role", qualifiedByName = "getRoleDescription")
    CommentInfo toCommentInfo(QueryOrderComment orderCommentDTOs);

    default Order toOrder(MakeOrderRequest requestDTO, Member member, Team team, Product product) {
        return Order.builder()
                .view(0)
                .memo(requestDTO.memo())
                .date(requestDTO.date())
                .orderStatus(OrderStatus.PENDING_CONFIRM)
                .orderAmount(requestDTO.orderAmount())
                .product(product)
                .member(member)
                .team(team)
                .build();
    }

    @Mapping(source = "order", target = "order")
    @Mapping(source = "member", target = "member")
    OrderMention toOrderMention(Order order, Member member);

    MakeOrderResponse toMakeOrderResponse(Long orderId);

    GetOrderResponse toGetOrderResponse(QueryOrder queryOrder);

    @Named("getRoleDescription")
    default String getRoleDescription(TeamMemberRole teamMemberRole) {
        return teamMemberRole.getDescription();
    }

    default GetOrderCommentsResponse toGetOrderCommentsResponse(List<QueryOrderComment> orderComments) {
        return GetOrderCommentsResponse.builder()
                .comments(orderComments.stream()
                        .map(this::toCommentInfo)
                        .toList())
                .build();
    }
}
