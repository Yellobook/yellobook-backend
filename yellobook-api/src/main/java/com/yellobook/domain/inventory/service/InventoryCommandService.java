package com.yellobook.domain.inventory.service;

import com.yellobook.common.utils.TeamUtil;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.AddProductResponse;
import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;
import com.yellobook.domain.inventory.mapper.ProductCustomMapper;
import com.yellobook.domain.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.enums.MemberTeamRole;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.code.InventoryErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryCommandService{
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryAuthQueryService inventoryAuthQueryService;
    private final TeamUtil teamUtil;
    private final ProductMapper productMapper;

    /**
     * 제품 추가 (관리자)
     */
    public AddProductResponse addProduct(Long inventoryId, AddProductRequest requestDTO, CustomOAuth2User oAuth2User) {
        Long memberId = oAuth2User.getMemberId();
        Long teamId  = teamUtil.getCurrentTeam(memberId);
        MemberTeamRole role = inventoryAuthQueryService.getMemberTeamRole(teamId, memberId);
        inventoryAuthQueryService.forbidViewer(role);
        inventoryAuthQueryService.forbidOrderer(role);

        // SKU 중복 확인 (inventory & sku)
        if(productRepository.existsByInventoryIdAndSku(inventoryId, requestDTO.getSku())){
            throw new CustomException(InventoryErrorCode.SKU_ALREADY_EXISTS);
        }

        Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
        if(inventoryOptional.isEmpty()){
            throw new CustomException(InventoryErrorCode.INVENTORY_NOT_FOUND);
        }
        Product newProduct = productMapper.INSTANCE.toProduct(requestDTO);
        newProduct.modifyInventory(inventoryOptional.get());
        Long productId = productRepository.save(newProduct).getId();
        return AddProductResponse.builder().productId(productId).build();
    }

    /**
     * 제품 수량 수정 (관리자)
     */
    public void modifyProductAmount(Long productId, ModifyProductAmountRequest requestDTO, CustomOAuth2User oAuth2User) {
        Long memberId = oAuth2User.getMemberId();
        Long teamId  = teamUtil.getCurrentTeam(memberId);
        MemberTeamRole role = inventoryAuthQueryService.getMemberTeamRole(teamId, memberId);
        inventoryAuthQueryService.forbidViewer(role);
        inventoryAuthQueryService.forbidOrderer(role);

        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new CustomException(InventoryErrorCode.PRODUCT_NOT_FOUND);  // 여기 이미 validation 했는데, 또 해야 하나...?
        }
        productOptional.get().modifyAmount(requestDTO.getAmount());
    }

    /**
     * 제품 삭제 (관리자)
     */
    public void deleteProduct(Long productId, CustomOAuth2User oAuth2User) {
        Long memberId = oAuth2User.getMemberId();
        Long teamId  = teamUtil.getCurrentTeam(memberId);
        MemberTeamRole role = inventoryAuthQueryService.getMemberTeamRole(teamId, memberId);
        inventoryAuthQueryService.forbidViewer(role);
        inventoryAuthQueryService.forbidOrderer(role);

        productRepository.deleteById(productId);
    }

}
