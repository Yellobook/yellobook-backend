package com.yellobook.domains.order.mapper;

import static com.yellobook.domains.order.dto.response.GetOrderCommentsResponse.CommentInfo;

import com.yellobook.common.enums.OrderStatus;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.dto.query.QueryOrder;
import com.yellobook.domains.order.dto.query.QueryOrderComment;
import com.yellobook.domains.order.dto.request.AddOrderCommentRequest;
import com.yellobook.domains.order.dto.request.MakeOrderRequest;
import com.yellobook.domains.order.dto.response.AddOrderCommentResponse;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domains.order.dto.response.GetOrderResponse;
import com.yellobook.domains.order.dto.response.MakeOrderResponse;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import com.yellobook.domains.order.entity.OrderMention;
import com.yellobook.domains.team.entity.Team;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {OrderStatus.class})
public interface OrderMapper {

    @Mapping(source = "member", target = "member")
    @Mapping(source = "order", target = "order")
    OrderComment toOrderComment(AddOrderCommentRequest requestDTO, Member member, Order order);

    AddOrderCommentResponse toAddOrderCommentResponse(Long commentId);

    @Mapping(target = "view", constant = "0")
    @Mapping(source = "member", target = "member")
    @Mapping(source = "team", target = "team")
    @Mapping(source = "product", target = "product")
    @Mapping(target = "orderStatus", expression = "java(OrderStatus.PENDING_CONFIRM)")
    Order toOrder(MakeOrderRequest request, Member member, Team team, Product product);

    @Mapping(source = "order", target = "order")
    @Mapping(source = "member", target = "member")
    OrderMention toOrderMention(Order order, Member member);

    MakeOrderResponse toMakeOrderResponse(Long orderId);

    GetOrderResponse toGetOrderResponse(QueryOrder queryOrder);

    @Mapping(source = "orderComments", target = "comments")
    GetOrderCommentsResponse toGetOrderCommentsResponse(Long orderId, List<QueryOrderComment> orderComments);

    @Mapping(target = "role", expression = "java(comment.role().getDescription())")
    CommentInfo toQueryOrderComment(QueryOrderComment comment);
}
