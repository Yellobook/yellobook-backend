package com.yellobook.domains.inventory.service;

import com.yellobook.common.utils.ExcelReadUtil;
import com.yellobook.common.utils.ParticipantUtil;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.dto.cond.ExcelProductCond;
import com.yellobook.domains.inventory.dto.request.AddProductRequest;
import com.yellobook.domains.inventory.dto.response.AddInventoryResponse;
import com.yellobook.domains.inventory.dto.response.AddProductResponse;
import com.yellobook.domains.inventory.dto.request.ModifyProductAmountRequest;
import com.yellobook.domains.inventory.mapper.InventoryMapper;
import com.yellobook.domains.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.error.code.FileErrorCode;
import com.yellobook.error.code.InventoryErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InventoryCommandService{
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final InventoryMapper inventoryMapper;

    /**
     * 제품 추가 (관리자)
     */
    public AddProductResponse addProduct(Long inventoryId, AddProductRequest requestDTO, TeamMemberVO teamMember) {
        ParticipantUtil.forbidViewer(teamMember.getRole());
        ParticipantUtil.forbidOrderer(teamMember.getRole());

        // SKU 중복 확인 (inventory & sku)
        if(productRepository.existsByInventoryIdAndSku(inventoryId, requestDTO.sku())){
            throw new CustomException(InventoryErrorCode.SKU_ALREADY_EXISTS);
        }

        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new CustomException(InventoryErrorCode.INVENTORY_NOT_FOUND) );
        Product newProduct = productMapper.toProduct(requestDTO, inventory);
        Long productId = productRepository.save(newProduct).getId();
        return productMapper.toAddProductResponse(productId);
    }

    /**
     * 제품 수량 수정 (관리자)
     */
    public void modifyProductAmount(Long productId, ModifyProductAmountRequest requestDTO, TeamMemberVO teamMember) {
        ParticipantUtil.forbidViewer(teamMember.getRole());
        ParticipantUtil.forbidOrderer(teamMember.getRole());

        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new CustomException(InventoryErrorCode.PRODUCT_NOT_FOUND);  // 여기 이미 validation 했는데, 또 해야 하나...?
        }
        productOptional.get().modifyAmount(requestDTO.amount());
    }

    /**
     * 제품 삭제 (관리자)
     */
    public void deleteProduct(Long productId, TeamMemberVO teamMember) {
        ParticipantUtil.forbidViewer(teamMember.getRole());
        ParticipantUtil.forbidOrderer(teamMember.getRole());

        productRepository.deleteById(productId);
    }

    /**
     * 재고 추가 (관리자)
     */
    public AddInventoryResponse addInventory(MultipartFile file) {
//        ParticipantUtil.forbidViewer(teamMember.getRole());
//        ParticipantUtil.forbidOrderer(teamMember.getRole());
        // 엑셀 파일 이름 저장
        String fileName = file.getOriginalFilename();
        log.info("[excel] fileName : {}", fileName);

        List<ExcelProductCond> productConds;
        try{
            productConds = ExcelReadUtil.read(file);
        }catch (IOException e){
            throw new CustomException(FileErrorCode.FILE_IO_FAIL);
        }

        // 반복문으로 로그 출력
        for (ExcelProductCond productCond : productConds) {
            log.info("[excel] {}", productCond);
        }

        return null;
        //return inventoryMapper.toAddInventoryResponse(inventoryId);
    }
}
