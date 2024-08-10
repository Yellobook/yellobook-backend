package com.yellobook.domains.inventory.service;

import com.yellobook.common.utils.ParticipantUtil;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse.ProductInfo;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse;
import com.yellobook.domains.inventory.mapper.InventoryMapper;
import com.yellobook.domains.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryQueryService{
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductMapper productMapper;

    /**
     * 전체 재고 현황 확인 API (관리자, 주문자)
     */
    public GetTotalInventoryResponse getTotalInventory(Integer page, Integer size, TeamMemberVO teamMember) {
        ParticipantUtil.forbidViewer(teamMember.getRole());

        Pageable pageable = PageRequest.of(page-1, size);
        List<QueryInventory> content = inventoryRepository.getTotalInventory(teamMember.getTeamId(), pageable);
        return GetTotalInventoryResponse.builder()
                .page(page).size(content.size())
                .inventories(inventoryMapper.toInventoryInfoList(content))
                .build();
    }

    /**
     * 특정 재고 일자 현황 조회 (관리자, 주문자)
     */
    public GetProductsResponse getProductsByInventory(Long inventoryId, TeamMemberVO teamMember) {
        // 존재하는 재고 인지 확인, 접근 권환 확인
        // 제품 이름 순으로 정렬해서 보여주기 (페이징 X)
        ParticipantUtil.forbidViewer(teamMember.getRole());

        List<ProductInfo> content = productMapper.toProductInfo(productRepository.getProducts(inventoryId));
        return GetProductsResponse.builder().products(content).build();
    }

    /**
     * 재고 검색 (관리자)
     */
    public GetProductsResponse getProductByKeywordAndInventory(Long inventoryId, String keyword, TeamMemberVO teamMember) {
        // 존재하는 재고인지 확인, 접근 권한 확인
        // 제품 이름 순으로 정렬해서 보여주기 (페이징 X)
        ParticipantUtil.forbidViewer(teamMember.getRole());
        ParticipantUtil.forbidOrderer(teamMember.getRole());

        List<ProductInfo> content = productMapper.toProductInfo(productRepository.getProducts(inventoryId, keyword));
        return GetProductsResponse.builder().products(content).build();
    }

    public boolean existByInventoryId(Long inventoryId){
        return inventoryRepository.existsById(inventoryId);
    }

    public boolean existByProductId(Long productId){
        return productRepository.existsById(productId);
    }

}
