package com.yellobook.domain.inventory.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.utils.ParticipantUtil;
import com.yellobook.common.utils.TeamUtil;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.AddProductResponse;
import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;
import com.yellobook.domain.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
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
    private final ParticipantUtil participantUtil;
    private final TeamUtil teamUtil;
    private final ProductMapper productMapper;

    /**
     * 제품 추가 (관리자)
     */
    public AddProductResponse addProduct(Long inventoryId, AddProductRequest requestDTO, CustomOAuth2User oAuth2User) {
        Long memberId = oAuth2User.getMemberId();
        Long teamId  = teamUtil.getCurrentTeam(memberId);
        MemberTeamRole role = participantUtil.getMemberTeamRole(teamId, memberId);
        participantUtil.forbidViewer(role);
        participantUtil.forbidOrderer(role);

        // SKU 중복 확인 (inventory & sku)
        if(productRepository.existsByInventoryIdAndSku(inventoryId, requestDTO.getSku())){
            throw new CustomException(InventoryErrorCode.SKU_ALREADY_EXISTS);
        }

        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new CustomException(InventoryErrorCode.INVENTORY_NOT_FOUND) );
        Product newProduct = productMapper.toProduct(requestDTO);
        newProduct.modifyInventory(inventory);
        Long productId = productRepository.save(newProduct).getId();
        return AddProductResponse.builder().productId(productId).build();
    }

    /**
     * 제품 수량 수정 (관리자)
     */
    public void modifyProductAmount(Long productId, ModifyProductAmountRequest requestDTO, CustomOAuth2User oAuth2User) {
        Long memberId = oAuth2User.getMemberId();
        Long teamId  = teamUtil.getCurrentTeam(memberId);
        MemberTeamRole role = participantUtil.getMemberTeamRole(teamId, memberId);
        participantUtil.forbidViewer(role);
        participantUtil.forbidOrderer(role);

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
        MemberTeamRole role = participantUtil.getMemberTeamRole(teamId, memberId);
        participantUtil.forbidViewer(role);
        participantUtil.forbidOrderer(role);

        productRepository.deleteById(productId);
    }

}
