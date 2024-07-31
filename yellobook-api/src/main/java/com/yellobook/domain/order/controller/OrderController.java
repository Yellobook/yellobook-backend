package com.yellobook.domain.order.controller;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.order.service.OrderCommandService;
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
@Tag(name = "\uD83E\uDDFE 주문" , description = "Order API")
public class OrderController {
    private final OrderCommandService orderCommandService;

    @Operation(summary = "주문 정정 요청")
    @PatchMapping("/{orderId}/correction")
    public ResponseEntity<SuccessResponse<String>> modifyRequestOrder(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
            ) {
        orderCommandService.modifyRequestOrder(orderId, oAuth2User);
        return null;
    }

    @Operation(summary = "주문 확인")
    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<SuccessResponse<String>> confirmOrder(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        orderCommandService.confirmOrder(orderId, oAuth2User);
        return null;
    }

    @Operation(summary = "주문 취소")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<String>> cancelOrder(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ) {
        orderCommandService.cancelOrder(orderId, oAuth2User);
        return null;
    }

}
