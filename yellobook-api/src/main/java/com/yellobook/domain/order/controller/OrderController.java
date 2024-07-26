package com.yellobook.domain.order.controller;

import com.yellobook.common.annotation.ExistOrder;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.order.dto.*;
import com.yellobook.domain.order.service.OrderCommandService;
import com.yellobook.domain.order.service.OrderQueryService;
import com.yellobook.domain.order.dto.GetOrderCommentsResponse;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = " \uD83E\uDDD9\uD83C\uDFFB\u200D 주문" , description = "Order API")
@Validated
public class OrderController {
    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    @Operation(summary = "[주문자] 주문 작성")
    @PostMapping("")
    public ResponseEntity<SuccessResponse<MakeOrderResponse>> makeOrder(
            @RequestBody MakeOrderRequest requestDTO,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        orderCommandService.makeOrder(requestDTO, oAuth2User.getMemberId());
        return null;
    }

    @Operation(summary = "[주문자, 관리자] 주문 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<?>> getOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        return null;
    }

    @Operation(summary = "[주문자, 관리자] 주문에 댓글 달기")
    @PostMapping("/{orderId}/comment")
    public ResponseEntity<SuccessResponse<AddOrderCommentResponse>> addOrderComment(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @RequestBody AddOrderCommentRequest requestDTO,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        AddOrderCommentResponse response = orderCommandService.addOrderComment(orderId, oAuth2User.getMemberId(), requestDTO);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[주문자, 관리자] 주문 댓글 조회")
    @GetMapping("/{orderId}/comment")
    public ResponseEntity<SuccessResponse<GetOrderCommentsResponse>> getOrderComments(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        GetOrderCommentsResponse response = orderQueryService.getOrderComments(orderId, oAuth2User.getMemberId());
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[관리자] 주문 정정 요청")
    @PatchMapping("/{orderId}/correction")
    public ResponseEntity<SuccessResponse<String>> modifyRequestOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
            ) {
        orderCommandService.modifyRequestOrder(orderId, oAuth2User.getMemberId());
        return ResponseFactory.success("주문의 정정 요청을 완료했습니다.");
    }

    @Operation(summary = "[관리자] 주문 확정")
    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<SuccessResponse<String>> confirmOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        orderCommandService.confirmOrder(orderId, oAuth2User.getMemberId());
        return ResponseFactory.success("주문의 확정을 완료했습니다.");
    }

    @Operation(summary = "[주문자] 주문 취소")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<String>> cancelOrder(
            @ExistOrder @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        orderCommandService.cancelOrder(orderId, oAuth2User.getMemberId());
        return ResponseFactory.success("주문 최소를 완료했습니다.");
    }

}
