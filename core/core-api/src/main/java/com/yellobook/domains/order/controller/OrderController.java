package com.yellobook.domains.order.controller;

import com.yellobook.common.resolver.annotation.TeamMember;
import com.yellobook.common.validation.annotation.ExistOrder;
import com.yellobook.domains.order.dto.request.AddOrderCommentRequest;
import com.yellobook.domains.order.dto.request.MakeOrderRequest;
import com.yellobook.domains.order.dto.response.AddOrderCommentResponse;
import com.yellobook.domains.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.domains.order.dto.response.GetOrderResponse;
import com.yellobook.domains.order.dto.response.MakeOrderResponse;
import com.yellobook.domains.order.service.OrderCommandService;
import com.yellobook.domains.order.service.OrderQueryService;
import com.yellobook.support.response.ResponseFactory;
import com.yellobook.support.response.SuccessResponse;
import com.yellobook.common.vo.TeamMemberVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
@Tag(name = "\uD83E\uDDFE 주문", description = "Order API")
public class OrderController {
    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    @Operation(summary = "[주문자] 주문 작성")
    @PostMapping("")
    public ResponseEntity<SuccessResponse<MakeOrderResponse>> makeOrder(
            @Valid @RequestBody MakeOrderRequest requestDTO,
            @TeamMember TeamMemberVO teamMember
    ) {
        MakeOrderResponse response = orderCommandService.makeOrder(requestDTO, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[주문자, 관리자] 주문 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<GetOrderResponse>> getOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ) {
        GetOrderResponse response = orderQueryService.getOrder(orderId, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[주문자, 관리자] 주문에 댓글 달기")
    @PostMapping("/{orderId}/comment")
    public ResponseEntity<SuccessResponse<AddOrderCommentResponse>> addOrderComment(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @Valid @RequestBody AddOrderCommentRequest requestDTO,
            @TeamMember TeamMemberVO teamMember
    ) {
        AddOrderCommentResponse response = orderCommandService.addOrderComment(orderId, teamMember, requestDTO);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[주문자, 관리자] 주문 댓글 조회")
    @GetMapping("/{orderId}/comment")
    public ResponseEntity<SuccessResponse<GetOrderCommentsResponse>> getOrderComments(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ) {
        GetOrderCommentsResponse response = orderQueryService.getOrderComments(orderId, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[관리자] 주문 정정 요청")
    @PatchMapping("/{orderId}/correction")
    public ResponseEntity<Void> modifyRequestOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ) {
        orderCommandService.modifyRequestOrder(orderId, teamMember);
        return ResponseFactory.noContent();
    }

    @Operation(summary = "[관리자] 주문 확정")
    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ) {
        orderCommandService.confirmOrder(orderId, teamMember);
        return ResponseFactory.noContent();
    }

    @Operation(summary = "주문 취소")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ) {
        orderCommandService.cancelOrder(orderId, teamMember);
        return ResponseFactory.noContent();
    }

}
