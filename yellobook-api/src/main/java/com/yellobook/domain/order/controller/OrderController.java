package com.yellobook.domain.order.controller;

import com.yellobook.domain.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "\uD83D\uDCE6 주문", description = "Order API")
public class OrderController {
    private final OrderService orderService;
}
