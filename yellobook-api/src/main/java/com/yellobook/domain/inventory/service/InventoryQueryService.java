package com.yellobook.domain.inventory.service;

import com.yellobook.common.utils.TeamUtil;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domain.inventory.mapper.InventoryMapper;
import com.yellobook.domain.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.dto.InventoryDTO;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.code.InventoryErrorCode;
import com.yellobook.error.exception.CustomException;
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
    private final InventoryAuthQueryService inventoryAuthQueryService;
    private final InventoryMapper inventoryMapper;
    private final ProductMapper productMapper;
    private final TeamUtil teamUtil;

    /**
     * 전체 재고 현황 확인 API (관리자, 주문자)
     */
    public GetTotalInventoryResponse getTotalInventory(Integer page, Integer size, CustomOAuth2User oAuth2User) {
        Long memberId = oAuth2User.getMemberId();
        Long teamId  = teamUtil.getCurrentTeam(memberId);
        if(inventoryAuthQueryService.isViewer(teamId, memberId)){
            // 뷰어는 권한 없음
            throw new CustomException(AuthErrorCode.VIEWER_NOT_ALLOWED);
        }
        Pageable pageable = PageRequest.of(page-1, size);
        List<InventoryDTO> content = inventoryRepository.getTotalInventory(teamId, pageable);
        return GetTotalInventoryResponse.builder()
                .page(page).size(content.size())
                .inventories(inventoryMapper.toInventoryInfoList(content))
                .build();
    }

    /**
     * 특정 제고 일자 현황 조회 (관리자, 주문자)
     */
    public GetProductsResponse getProductsByInventory(Long inventoryId, CustomOAuth2User oAuth2User) {
        // 존재하는 재고 인지 확인, 접근 권환 확인
        // 제품 이름 순으로 정렬해서 보여주기 (페이징 X)
        Long memberId = oAuth2User.getMemberId();
        Long teamId  = teamUtil.getCurrentTeam(memberId);
        if(inventoryRepository.existsById(inventoryId)){
            throw new CustomException(InventoryErrorCode.INVENTORY_NOT_FOUND);
        }
        if(inventoryAuthQueryService.isViewer(teamId, memberId)){
            throw new CustomException(AuthErrorCode.VIEWER_NOT_ALLOWED);
        }
        List<GetProductsResponse.ProductInfo> content = productMapper.toProductInfo(productRepository.getProducts(inventoryId));
        return GetProductsResponse.builder().products(content).build();
    }

    /**
     * 재고 검색 (관리자)
     */
    public GetProductsResponse getProductByKeywordAndInventory(Long inventoryId, String keyword, CustomOAuth2User oAuth2User) {
        // 존재하는 재고인지 확인, 접근 권한 확인
        // 제품 이름 순으로 정렬해서 보여주기 (페이징 X)
        Long memberId = oAuth2User.getMemberId();
        Long teamId  = teamUtil.getCurrentTeam(memberId);
        if(inventoryRepository.existsById(inventoryId)){
            throw new CustomException(InventoryErrorCode.INVENTORY_NOT_FOUND);
        }
        if(inventoryAuthQueryService.isViewer(teamId, memberId)){
            throw new CustomException(AuthErrorCode.VIEWER_NOT_ALLOWED);
        }
        List<GetProductsResponse.ProductInfo> content = productMapper.toProductInfo(productRepository.getProducts(inventoryId, keyword));
        return GetProductsResponse.builder().products(content).build();
    }

}
