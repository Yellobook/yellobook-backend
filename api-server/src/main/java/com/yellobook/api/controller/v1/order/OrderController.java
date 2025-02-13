package com.yellobook.api.controller.v1.order;

import com.yellobook.api.controller.v1.order.dto.request.CreateOrderRequest;
import com.yellobook.api.controller.v1.order.dto.request.CustomerOrdersRequest;
import com.yellobook.api.controller.v1.order.dto.request.SellerOrdersRequest;
import com.yellobook.api.controller.v1.order.dto.response.CreateOrderResponse;
import com.yellobook.api.controller.v1.order.dto.response.CustomerOrdersResponse;
import com.yellobook.api.controller.v1.order.dto.response.OrderDetailResponse;
import com.yellobook.api.controller.v1.order.dto.response.SellerOrdersResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.pagination.CursorPageResponse;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.core.domain.order.OrderService;
import com.yellobook.core.domain.order.StoreOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/stores/{storeId}/orders")
@Validated
public class OrderController implements OrderApiDocs {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<CreateOrderResponse> createOrder(
            @PathVariable @NotNull @Min(1) Long storeId,
            @Valid @RequestBody CreateOrderRequest request,
            ApiMember apiMember
    ) {
        var result = orderService.createOrder(apiMember.toMember(), request.toNewOrder(storeId));
        return ApiResponse.success(CreateOrderResponse.of(result));
    }

    @PatchMapping("/{orderId}/approve")
    public ApiResponse<?> approveOrder(
            @PathVariable @NotNull @Min(1) Long storeId,
            @PathVariable @NotNull @Min(1) Long orderId,
            ApiMember apiMember
    ) {
        orderService.approveOrder(apiMember.toMember(), StoreOrder.of(storeId, orderId));
        return ApiResponse.success();
    }

    @PatchMapping("/{orderId}/reject")
    public ApiResponse<?> rejectOrder(
            @PathVariable @NotNull @Min(1) Long storeId,
            @PathVariable @NotNull @Min(1) Long orderId,
            ApiMember apiMember
    ) {
        orderService.rejectOrder(apiMember.toMember(), StoreOrder.of(storeId, orderId));
        return ApiResponse.success();
    }

    @PatchMapping("/{orderId}/cancel")
    public ApiResponse<?> cancelOrder(
            @PathVariable @NotNull @Min(1) Long storeId,
            @PathVariable @NotNull @Min(1) Long orderId,
            ApiMember apiMember
    ) {
        orderService.cancelOrder(apiMember.toMember(), StoreOrder.of(storeId, orderId));
        return ApiResponse.success();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDetailResponse> getOrderDetail(
            @PathVariable @NotNull @Min(1) Long storeId,
            @PathVariable @NotNull @Min(1) Long orderId,
            ApiMember apiMember
    ) {
        var result = orderService.getOrderDetail(apiMember.toMember(), StoreOrder.of(storeId, orderId));
        return ApiResponse.success(OrderDetailResponse.of(result));
    }

    @GetMapping("/seller")
    public ApiResponse<CursorPageResponse<SellerOrdersResponse>> getOrdersForSeller(
            @Valid @ModelAttribute SellerOrdersRequest request,
            @PathVariable @NotNull @Min(1) Long storeId,
            ApiMember apiMember
    ) {
        var result = orderService.getOrders(apiMember.toMember(), request.toOrdersCriteria(storeId));
        return ApiResponse.success(
                CursorPageResponse.of(SellerOrdersResponse.fromList(result.data()), request.getCursorId(),
                        result.nextCursorId()));
    }

    @GetMapping("/customer")
    public ApiResponse<CursorPageResponse<CustomerOrdersResponse>> getOrdersForCustomer(
            @Valid @ModelAttribute CustomerOrdersRequest request,
            @PathVariable @NotNull @Min(1) Long storeId,
            ApiMember apiMember
    ) {
        var result = orderService.getOrders(apiMember.toMember(), request.toOrdersCriteria(storeId));
        return ApiResponse.success(
                CursorPageResponse.of(CustomerOrdersResponse.fromList(result.data()), request.getCursorId(),
                        result.nextCursorId()));
    }
}
