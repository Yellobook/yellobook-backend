package com.yellobook.domain.order.controller;

import com.yellobook.common.annotation.ExistOrder;
import com.yellobook.common.annotation.TeamMember;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.order.dto.request.AddOrderCommentRequest;
import com.yellobook.domain.order.dto.request.MakeOrderRequest;
import com.yellobook.domain.order.dto.response.AddOrderCommentResponse;
import com.yellobook.domain.order.dto.response.GetOrderResponse;
import com.yellobook.domain.order.dto.response.MakeOrderResponse;
import com.yellobook.domain.order.service.OrderCommandService;
import com.yellobook.domain.order.service.OrderQueryService;
import com.yellobook.domain.order.dto.response.GetOrderCommentsResponse;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
@Tag(name = "\uD83E\uDDFE 주문" , description = "Order API")
public class OrderController {
    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    @Operation(summary = "[주문자] 주문 작성")
    @PostMapping("")
    public ResponseEntity<SuccessResponse<MakeOrderResponse>> makeOrder(
            @RequestBody MakeOrderRequest requestDTO,
            @TeamMember TeamMemberVO teamMember
    ){
        MakeOrderResponse response = orderCommandService.makeOrder(requestDTO, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[주문자, 관리자] 주문 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<GetOrderResponse>> getOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ){
        GetOrderResponse response = orderQueryService.getOrder(orderId, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[주문자, 관리자] 주문에 댓글 달기")
    @PostMapping("/{orderId}/comment")
    public ResponseEntity<SuccessResponse<AddOrderCommentResponse>> addOrderComment(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @RequestBody AddOrderCommentRequest requestDTO,
            @TeamMember TeamMemberVO teamMember
    ){
        AddOrderCommentResponse response = orderCommandService.addOrderComment(orderId, teamMember, requestDTO);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[주문자, 관리자] 주문 댓글 조회")
    @GetMapping("/{orderId}/comment")
    public ResponseEntity<SuccessResponse<GetOrderCommentsResponse>> getOrderComments(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ){
        GetOrderCommentsResponse response = orderQueryService.getOrderComments(orderId,teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[관리자] 주문 정정 요청")
    @PatchMapping("/{orderId}/correction")
    public ResponseEntity<SuccessResponse<String>> modifyRequestOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
            ) {
        orderCommandService.modifyRequestOrder(orderId, teamMember);
        return ResponseFactory.success("주문의 정정 요청을 완료했습니다.");
    }

    @Operation(summary = "[관리자] 주문 확정")
    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<SuccessResponse<String>> confirmOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ) {
        orderCommandService.confirmOrder(orderId, teamMember);
        return ResponseFactory.success("주문의 확정을 완료했습니다.");
    }

    @Operation(summary = "주문 취소")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<String>> cancelOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @TeamMember TeamMemberVO teamMember
    ) {
        orderCommandService.cancelOrder(orderId, teamMember);
        return ResponseFactory.success("주문 최소를 완료했습니다.");
    }

}
