package com.yellobook.domain.order.controller;

import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/{teamId}")
@RequiredArgsConstructor
@Tag(name = " \uD83E\uDDD9\uD83C\uDFFB\u200D 주문" , description = "Order API")
public class OrderController {

    @Operation(summary = "[관리자] 주문 정정 요청")
    @PatchMapping("/{orderId}/correction")
    public ResponseEntity<SuccessResponse<String>> requestModifyOrder(
            @PathVariable("teamId") Long teamId,
            @PathVariable("orderId") Long orderId
    ){
        return null;
    }

    @Operation(summary = "[관리자] 주문 확인")
    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<SuccessResponse<String>> confirmOrder(
            @PathVariable("teamId") Long teamId,
            @PathVariable("orderId") Long orderId
    ){
        return null;
    }

    @Operation(summary = "[주문자] 주문 취소")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<String>> cancelOrder(
            @PathVariable("teamId") Long teamId,
            @PathVariable("orderId") Long orderId
    ){
        return null;
    }

}
