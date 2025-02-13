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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "ORDER API", description = "Order Endpoints")
public interface OrderApiDocs {

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = CreateOrderResponse.class))
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "ORDER01")
    })
    ApiResponse<CreateOrderResponse> createOrder(
            @Parameter(description = "주문이 속한 가게의 ID", example = "10")
            @PathVariable Long storeId,
            @RequestBody CreateOrderRequest request,
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "주문 승인", description = "판매자가 주문을 승인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    ApiResponse<?> approveOrder(
            @Parameter(description = "주문이 속한 가게의 ID", example = "10")
            @PathVariable Long storeId,
            @Parameter(description = "승인할 주문의 ID", example = "1024")
            @PathVariable Long orderId,
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "주문 거절", description = "판매자가 주문을 거절합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    ApiResponse<?> rejectOrder(
            @Parameter(description = "주문이 속한 가게의 ID", example = "10")
            @PathVariable Long storeId,
            @Parameter(description = "거절할 주문의 ID", example = "1024")
            @PathVariable Long orderId,
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "주문 취소", description = "사용자가 주문을 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    ApiResponse<?> cancelOrder(
            @Parameter(description = "주문이 속한 가게의 ID", example = "10")
            @PathVariable Long storeId,
            @Parameter(description = "취소할 주문의 ID", example = "1024")
            @PathVariable Long orderId,
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = OrderDetailResponse.class))
                            }
                    )
            )
    })
    ApiResponse<OrderDetailResponse> getOrderDetail(
            @Parameter(description = "주문이 속한 가게의 ID", example = "10")
            @PathVariable Long storeId,
            @Parameter(description = "조회할 주문의 ID", example = "1024")
            @PathVariable Long orderId,
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "판매자의 주문 목록 조회", description = "판매자가 관리하는 주문 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "data", array = @ArraySchema(
                                            schema = @Schema(implementation = SellerOrdersResponse.class)
                                    )),
                                    @SchemaProperty(name = "nextCursorId", schema = @Schema(type = "integer", example = "1024")),
                                    @SchemaProperty(name = "hasNext", schema = @Schema(type = "boolean", example = "true")),
                                    @SchemaProperty(name = "isFirstPage", schema = @Schema(type = "boolean", example = "true")),
                                    @SchemaProperty(name = "isLastPage", schema = @Schema(type = "boolean", example = "false"))
                            }
                    )
            )
    })
    ApiResponse<CursorPageResponse<SellerOrdersResponse>> getOrdersForSeller(
            @ModelAttribute SellerOrdersRequest request,
            @Parameter(description = "가게 ID", example = "10")
            @PathVariable Long storeId,
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "주문자의 주문 목록 조회", description = "주문자가 주문한 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "data", array = @ArraySchema(
                                            schema = @Schema(implementation = CustomerOrdersResponse.class)
                                    )),
                                    @SchemaProperty(name = "nextCursorId", schema = @Schema(type = "integer", example = "1024")),
                                    @SchemaProperty(name = "hasNext", schema = @Schema(type = "boolean", example = "true")),
                                    @SchemaProperty(name = "isFirstPage", schema = @Schema(type = "boolean", example = "true")),
                                    @SchemaProperty(name = "isLastPage", schema = @Schema(type = "boolean", example = "false"))
                            }
                    )
            )
    })
    ApiResponse<CursorPageResponse<CustomerOrdersResponse>> getOrdersForCustomer(
            @ModelAttribute CustomerOrdersRequest request,
            @Parameter(description = "가게 ID", example = "10")
            @PathVariable Long storeId,
            @Parameter(hidden = true) ApiMember apiMember
    );
}
