package com.yellobook.controller.inventory.controller;

import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.controller.inventory.dto.request.AddProductRequest;
import com.yellobook.controller.inventory.dto.request.ModifyProductAmountRequest;
import com.yellobook.controller.inventory.dto.response.AddInventoryResponse;
import com.yellobook.controller.inventory.dto.response.AddProductResponse;
import com.yellobook.controller.inventory.dto.response.GetProductsNameResponse;
import com.yellobook.controller.inventory.dto.response.GetProductsResponse;
import com.yellobook.controller.inventory.dto.response.GetSubProductNameResponse;
import com.yellobook.controller.inventory.dto.response.GetTotalInventoryResponse;
import com.yellobook.inventory.service.InventoryCommandService;
import com.yellobook.inventory.service.InventoryQueryService;
import com.yellobook.support.common.resolver.annotation.TeamMember;
import com.yellobook.support.common.validation.annotation.ExistInventory;
import com.yellobook.support.common.validation.annotation.ExistProduct;
import com.yellobook.support.response.ResponseFactory;
import com.yellobook.support.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/inventories")
@Tag(name = " \uD83D\uDCE6 재고", description = "Inventory API")
@RequiredArgsConstructor
@Validated
@Slf4j
public class InventoryController {

    private final InventoryCommandService inventoryCommandService;
    private final InventoryQueryService inventoryQueryService;

    @Operation(summary = "전체 재고 현황 글 조회")
    @GetMapping
    public ResponseEntity<SuccessResponse<GetTotalInventoryResponse>> getTotalInventory(
            @Min(value = 1, message = "page는 1 이상이여야 합니다.") @RequestParam("page") Integer page,
            @Min(value = 1, message = "size는 1 이상이여야 합니다.") @RequestParam("size") Integer size,
            @TeamMember TeamMemberVO teamMember
    ) {
        var result = inventoryQueryService.getTotalInventory(page, size, teamMember);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "일별 재고 현황 상세 조회")
    @GetMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductsByInventory(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @TeamMember TeamMemberVO teamMember
    ) {
        GetProductsResponse response = inventoryQueryService.getProductsByInventory(inventoryId, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "재고 조회수 증가")
    @PatchMapping("/{inventoryId}/views")
    public ResponseEntity<Void> increaseInventoryView(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @TeamMember TeamMemberVO teamMember
    ) {
        inventoryCommandService.increaseInventoryView(inventoryId, teamMember);
        return ResponseFactory.noContent();
    }

    @Operation(summary = "재고 검색")
    @GetMapping("/{inventoryId}/search")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductByKeywordAndInventory(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @RequestParam("keyword") String keyword,
            @TeamMember TeamMemberVO teamMember
    ) {
        GetProductsResponse response = inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword,
                teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "제품 추가")
    @PostMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<AddProductResponse>> addProduct(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @Valid @RequestBody AddProductRequest requestDTO,
            @TeamMember TeamMemberVO teamMember
    ) {
        AddProductResponse response = inventoryCommandService.addProduct(inventoryId, requestDTO, teamMember);
        return ResponseFactory.created(response);
    }

    @Operation(summary = "제품 수량 수정")
    @PutMapping("/products/{productId}")
    public ResponseEntity<Void> modifyProductAmount(
            @ExistProduct @PathVariable("productId") Long productId,
            @Valid @RequestBody ModifyProductAmountRequest requestDTO,
            @TeamMember TeamMemberVO teamMember
    ) {
        inventoryCommandService.modifyProductAmount(productId, requestDTO, teamMember);
        return ResponseFactory.noContent();
    }

    @Operation(summary = "제품 삭제")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @ExistProduct @PathVariable("productId") Long productId,
            @TeamMember TeamMemberVO teamMember
    ) {
        inventoryCommandService.deleteProduct(productId, teamMember);
        return ResponseFactory.noContent();
    }

    @Operation(summary = "엑셀 파일 읽어서 재고 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SuccessResponse<AddInventoryResponse>> addInventory(
            @RequestPart("file") MultipartFile file,
            @TeamMember TeamMemberVO teamMember
    ) {
        AddInventoryResponse response = inventoryCommandService.addInventory(file, teamMember);
        return ResponseFactory.created(response);
    }

    @Operation(summary = "제품 이름으로 제품 조회")
    @GetMapping("/products/search")
    public ResponseEntity<SuccessResponse<GetProductsNameResponse>> getProductNames(
            @RequestParam("name") String name,
            @TeamMember TeamMemberVO teamMember
    ) {
        GetProductsNameResponse response = inventoryQueryService.getProductsName(name, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "제품 이름으로 하위 제품 조회")
    @GetMapping("/subProducts/search")
    public ResponseEntity<SuccessResponse<GetSubProductNameResponse>> getSubProductName(
            @RequestParam("name") String name,
            @TeamMember TeamMemberVO teamMember
    ) {
        GetSubProductNameResponse response = inventoryQueryService.getSubProductName(name, teamMember);
        return ResponseFactory.success(response);
    }

}
