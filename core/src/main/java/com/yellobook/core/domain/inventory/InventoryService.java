package com.yellobook.core.domain.inventory;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.inventory.dto.CreateProductCommend;
import com.yellobook.core.domain.inventory.dto.CreateProductDetail;
import com.yellobook.core.domain.inventory.dto.ReadInventoryProductIds;
import com.yellobook.core.domain.inventory.dto.ReadInventoryProductQuery;
import com.yellobook.core.domain.inventory.dto.UpdateProductAmountCommend;
import com.yellobook.core.domain.team.TeamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryReader inventoryReader;
    private final InventoryWriter inventoryWriter;
    private final InventoryPermission inventoryPermission;
    private final ProductReader productReader;
    private final ProductWriter productWriter;
    private final TeamReader teamReader;


    public InventoryService(InventoryReader inventoryReader, InventoryWriter inventoryWriter,
                            InventoryPermission inventoryPermission, ProductReader productReader,
                            ProductWriter productWriter, TeamReader teamReader) {
        this.inventoryReader = inventoryReader;
        this.inventoryWriter = inventoryWriter;
        this.inventoryPermission = inventoryPermission;
        this.productReader = productReader;
        this.productWriter = productWriter;
        this.teamReader = teamReader;
    }

    /**
     * 전체 재고 현황 확인 API (관리자, 주문자)
     */
    public List<Inventory> getTotalInventory(Integer page, Integer size,
                                             Long teamId, TeamMemberRole role) {
        inventoryPermission.viewerCantAccess(role);
        return inventoryReader.readInventoriesByTeamId(teamId, page, size);
    }

    /**
     * 특정 재고 일자 현황 조회 (관리자, 주문자)
     */
    public ReadInventoryProductQuery getProductsByInventory(Long inventoryId,
                                                            TeamMemberRole role) {
        inventoryPermission.viewerCantAccess(role);
        Inventory inventory = inventoryReader.read(inventoryId);
        List<Product> products = productReader.readProductsByInventoryId(inventory.inventoryId());
        return new ReadInventoryProductQuery(inventory, products);
    }

    /**
     * 재고 중에 특정 keyword 를 제품 명에 "포함"하는 제품을 조회
     */
    public List<Product> searchProductByInventoryAndContainKeyword(Long inventoryId, String keyword,
                                                                   TeamMemberRole role) {
        // 존재하는 재고인지 확인, 접근 권한 확인
        inventoryPermission.viewerCantAccess(role);
        return productReader.readProductsByInventoryIdAndKeyword(inventoryId, keyword);
    }

    /**
     * 재고 중에 특정 keyword를 "포함"하는 제품을 찾음
     */
    public List<Product> searchProductsContainKeyword(String keyword, Long teamId, TeamMemberRole role) {
        inventoryPermission.viewerCantAccess(role);
        Long inventoryId = inventoryReader.readLastByTeamIdAndCreatedAt(teamId)
                .inventoryId();
        return productReader.readProductsByInventoryIdAndKeyword(inventoryId, keyword);
    }

    /**
     * 검색한 제품 이름과 "동일"한 제품들 조회
     */
    public List<Product> searchProductsByName(String name, Long teamId, TeamMemberRole role) {
        inventoryPermission.viewerCantAccess(role);
        // team의 가장 최근 인벤토리 가져옴
        Long inventoryId = inventoryReader.readLastByTeamIdAndCreatedAt(teamId)
                .inventoryId();
        // 재고가 존재하면 검색한 제품 이름과 "동일"한 제품들 조회
        return productReader.readProductsByInventoryIdAndName(inventoryId, name);
    }

    /**
     * 제품 추가 (관리자)
     */
    public Long addProduct(CreateProductCommend dto, TeamMemberRole role) {
        inventoryPermission.onlyAdminCanManipulate(role);
        Inventory inventory = inventoryReader.read(dto.inventoryId());
        productReader.existByInventoryIdAndSku(inventory.inventoryId(), dto.sku());
        inventoryWriter.updateUpdatedAt(inventory.inventoryId());
        return productWriter.create(dto);
    }

    /**
     * 제품 수량 수정 (관리자)
     */
    public void modifyProductAmount(UpdateProductAmountCommend dto, TeamMemberRole role) {
        inventoryPermission.onlyAdminCanManipulate(role);
        Product product = productReader.read(dto.productId());
        productWriter.updateAmount(dto.amount(), dto.productId());
        inventoryWriter.updateUpdatedAt(product.inventoryId());
    }

    /**
     * 제품 삭제 (관리자)
     */
    public void deleteProduct(Long productId, TeamMemberRole role) {
        inventoryPermission.onlyAdminCanManipulate(role);
        Product product = productReader.read(productId);
        // 주문에 연관되어 있는 제품이면 삭제 불가능
        productWriter.delete(product.productId());
        inventoryWriter.updateUpdatedAt(product.inventoryId());
    }

    /**
     * 재고 조회수 증가
     */
    public void increaseInventoryView(Long inventoryId, TeamMemberRole role) {
        inventoryPermission.viewerCantAccess(role);
        Inventory inventory = inventoryReader.read(inventoryId);
        inventoryWriter.increaseView(inventory.inventoryId());
    }

    /*
     * 재고 추가 (관리자)
     */
    public ReadInventoryProductIds createInventory(List<CreateProductDetail> productConds, Long teamId,
                                                   TeamMemberRole role) {
        inventoryPermission.onlyAdminCanManipulate(role);

        // 재고 저장
        teamReader.read(teamId);
        String title = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) + " 재고현황";
        Long inventoryId = inventoryWriter.create(teamId, title);
        //log.info("[EXCEL_INVENTORY] : id = {}, title = {}", newInventory.getId(), newInventory.getTitle());

        // 제품 저장
        List<Long> productIds = new ArrayList<>();
        for (CreateProductDetail productCond : productConds) {
            Long productId = productWriter.create(new CreateProductCommend(inventoryId, productCond));
            productIds.add(productId);
            //log.info("[EXCEL_PRODUCT] : id = {}", newProduct.getId());
        }

        return new ReadInventoryProductIds(inventoryId, productIds);
    }
}
