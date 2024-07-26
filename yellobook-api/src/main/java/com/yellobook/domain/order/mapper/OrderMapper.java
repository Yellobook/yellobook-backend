package com.yellobook.domain.order.mapper;

import com.yellobook.domain.order.dto.AddOrderCommentRequest;
import com.yellobook.domain.order.dto.AddOrderCommentResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.order.entity.Order;
import com.yellobook.domains.order.entity.OrderComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "requestDTO.content", target = "content")
    @Mapping(source = "member", target = "member")
    @Mapping(source = "order", target = "order")
    OrderComment toOrderComment(AddOrderCommentRequest requestDTO, Member member, Order order);

    AddOrderCommentResponse toAddOrderCommentResponse(Long commentId);
}
