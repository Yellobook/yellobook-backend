package com.yellobook.domain.inventory.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.AddProductResponse;
import com.yellobook.domain.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.code.InventoryErrorCode;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryCommandServiceTest {

    @InjectMocks
    private InventoryCommandService inventoryCommandService;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    private final TeamMemberVO admin = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
    private final TeamMemberVO orderer = TeamMemberVO.of(2L, 1L, MemberTeamRole.ORDERER);
    private final TeamMemberVO viewer = TeamMemberVO.of(3L, 1L, MemberTeamRole.VIEWER);

    @Nested
    @DisplayName("제품 추가")
    class AddProductTests{
        @Test
        @DisplayName("주문자는 제품 추가 불가능")
        void ordererCantAddProduct(){
            //given
            Long inventoryId = 1L;
            AddProductRequest request = createAddProductRequest();

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    inventoryCommandService.addProduct(inventoryId, request, orderer));
            Assertions.assertEquals(AuthErrorCode.ORDERER_NOT_ALLOWED, exception.getErrorCode());
        }


        @Test
        @DisplayName("뷰어는 제품 추가 불가능")
        void viewerCantAddProduct(){
            //given
            Long inventoryId = 1L;
            AddProductRequest request = createAddProductRequest();

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    inventoryCommandService.addProduct(inventoryId, request, viewer));
            Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
        }

        @Test
        @DisplayName("팀의 재품 중에서 SKU가 중복이면 제품 추가 불가능")
        void dupSKUCantAddProduct(){
            //given
            Long inventoryId = 1L;
            AddProductRequest request = createAddProductRequest();
            when(productRepository.existsByInventoryIdAndSku(1L, request.getSku())).thenReturn(true);

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    inventoryCommandService.addProduct(inventoryId, request, admin));
            Assertions.assertEquals(InventoryErrorCode.SKU_ALREADY_EXISTS, exception.getErrorCode());
        }

        @Test
        @DisplayName("재고(인벤토리)가 없으면 제품 추가 불가능")
        void NotExistInventoryCantAddProduct(){
            //given
            Long inventoryId = 1L;
            AddProductRequest request = createAddProductRequest();
            when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    inventoryCommandService.addProduct(inventoryId, request, admin));
            Assertions.assertEquals(InventoryErrorCode.INVENTORY_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("제품이 잘 추가되는지 확인")
        void addProduct(){
            //given
            Long inventoryId = 1L;
            AddProductRequest request = createAddProductRequest();
            Inventory inventory = createInventory();
            Product product = createProduct();
            when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
            when(productMapper.toProduct(request)).thenReturn(product);
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
                Product savedProduct = invocation.getArgument(0);
                Field idField = Product.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(savedProduct, 1L);
                return savedProduct;
            });

            //when
            AddProductResponse response = inventoryCommandService.addProduct(inventoryId, request, admin);

            //then
            assertThat(response).isNotNull();
            assertThat(response.getProductId()).isEqualTo(1L);
        }

        private AddProductRequest createAddProductRequest(){
            return AddProductRequest.builder()
                    .name("name")
                    .subProduct("sub")
                    .sku(111)
                    .purchasePrice(1000)
                    .salePrice(2000)
                    .amount(1000)
                    .build();
        }

    }

    @Nested
    @DisplayName("제품 수량 수정")
    class ModifyProductAmountTests{
//        @Test
//        @DisplayName("주문자는 제품 수량 수정 불가능")
//
//        @Test
//        @DisplayName("뷰어는 제품 수량 수정 불가능")
//
//        @Test
//        @DisplayName("제품 Id가 존재하지 않으면 제품 수량 수정 불가능")
//
//        @Test
//        @DisplayName("제품 수량 수정이 잘 되었는지 확인")
    }

    @Nested
    @DisplayName("제품 삭제")
    class DeleteProductTests{
//        @Test
//        @DisplayName("주문자는 제품 삭제 불가능")
//
//        @Test
//        @DisplayName("뷰어는 제품 삭제 불가능")
//
//        @Test
//        @DisplayName("제품이 잘 삭제되었는지 확인")
    }

    private Inventory createInventory(){
        return Inventory.builder().team(null).id(1L)
                .title("title")
                .view(100)
                .build();
    }

    private Product createProduct(){
        return Product.builder()
                .name("aa")
                .subProduct("aa")
                .sku(111)
                .purchasePrice(111)
                .salePrice(111)
                .amount(111)
                .build();
    }



}