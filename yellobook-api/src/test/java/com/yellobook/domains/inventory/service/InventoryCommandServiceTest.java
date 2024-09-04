package com.yellobook.domains.inventory.service;

import static fixture.InventoryFixture.createInventory;
import static fixture.InventoryFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.dto.request.AddProductRequest;
import com.yellobook.domains.inventory.dto.request.ModifyProductAmountRequest;
import com.yellobook.domains.inventory.dto.response.AddProductResponse;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.code.InventoryErrorCode;
import com.yellobook.error.exception.CustomException;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventoryCommandService Unit Test")
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
    @DisplayName("addProduct 메소드는")
    class Describe_AddProduct {
        @Nested
        @DisplayName("주문자라면")
        class Context_Orderer {
            Long inventoryId;
            AddProductRequest request;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                request = createAddProductRequest();
            }

            @Test
            @DisplayName("제품 추가 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.addProduct(inventoryId, request, orderer));
                Assertions.assertEquals(AuthErrorCode.ORDERER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("뷰어라면")
        class Context_Viewer {
            Long inventoryId;
            AddProductRequest request;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                request = createAddProductRequest();
            }

            @Test
            @DisplayName("제품 추가 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.addProduct(inventoryId, request, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("재고의 제품 중에서 SKU가 중복이면")
        class Context_SKU_Duplicate {
            Long inventoryId;
            AddProductRequest request;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                request = createAddProductRequest();
                when(productRepository.existsByInventoryIdAndSku(1L, request.sku())).thenReturn(true);
            }

            @Test
            @DisplayName("제품 추가 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.addProduct(inventoryId, request, admin));
                Assertions.assertEquals(InventoryErrorCode.SKU_ALREADY_EXISTS, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("해당 재고가 존재하지 않으면")
        class Context_Inventory_Not_Exist {
            Long inventoryId;
            AddProductRequest request;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                request = createAddProductRequest();
                when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("제품 추가 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.addProduct(inventoryId, request, admin));
                Assertions.assertEquals(InventoryErrorCode.INVENTORY_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("제품을 추가할 수 있으면")
        class Context_Can_Add_Product {
            Long inventoryId;
            AddProductRequest request;
            AddProductResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                request = createAddProductRequest();
                Inventory inventory = createInventory(null);
                Product product = createProduct(inventory);
                expectResponse = AddProductResponse.builder()
                        .productId(1L)
                        .build();
                when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
                when(productMapper.toProduct(request, inventory)).thenReturn(product);
                when(productRepository.save(product)).thenAnswer(invocation -> {
                    Product savedProduct = invocation.getArgument(0);
                    Field idField = Product.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(savedProduct, 1L);
                    return savedProduct;
                });
                when(productMapper.toAddProductResponse(any())).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("제품을 추가한다.")
            void it_adds_product() {
                AddProductResponse response = inventoryCommandService.addProduct(inventoryId, request, admin);

                assertThat(response).isNotNull();
                assertThat(response.productId()).isEqualTo(expectResponse.productId());
            }
        }

        private AddProductRequest createAddProductRequest() {
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
    @DisplayName("modifyProductAmount 메소드는")
    class Describe_ModifyProductAmount {
        @Nested
        @DisplayName("주문자라면")
        class Context_Orderer {
            Long productId;
            ModifyProductAmountRequest request;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
                request = createModifyProductAmountRequest();
            }

            @Test
            @DisplayName("제품 수량 수정이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.modifyProductAmount(productId, request, orderer));
                Assertions.assertEquals(AuthErrorCode.ORDERER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("뷰어라면")
        class Context_Viewer {
            Long productId;
            ModifyProductAmountRequest request;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
                request = createModifyProductAmountRequest();
            }

            @Test
            @DisplayName("제품 수량 수정이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.modifyProductAmount(productId, request, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("제품이 존재하지 않으면")
        class Context_Product_Not_Exist {
            Long productId;
            ModifyProductAmountRequest request;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
                request = createModifyProductAmountRequest();
                when(productRepository.findById(productId)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("제품 수량 수정이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.modifyProductAmount(productId, request, admin));
                Assertions.assertEquals(InventoryErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("제품 수량 수정이 가능하면")
        class Context_Can_Modify_Product_Amount {
            Long productId;
            ModifyProductAmountRequest request;
            Product product;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
                request = createModifyProductAmountRequest();
                product = createProduct(null);
                when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            }

            @Test
            @DisplayName("제품 수량 수정이 잘 되었는지 확인")
            void it_modify_product_amount() {
                inventoryCommandService.modifyProductAmount(productId, request, admin);

                assertThat(product.getAmount()).isEqualTo(request.amount());
            }
        }

        private ModifyProductAmountRequest createModifyProductAmountRequest() {
            return ModifyProductAmountRequest.builder()
                    .amount(1000)
                    .build();
        }
    }

    @Nested
    @DisplayName("deleteProduct 메소드는")
    class Describe_DeleteProduct {
        @Nested
        @DisplayName("주문자라면")
        class Context_Orderer {
            Long productId;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
            }

            @Test
            @DisplayName("제품 삭제가 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.deleteProduct(productId, orderer));
                Assertions.assertEquals(AuthErrorCode.ORDERER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("뷰어라면")
        class Context_Viewer {
            Long productId;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
            }

            @Test
            @DisplayName("제품 삭제가 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.deleteProduct(productId, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("제품 삭제가 가능하면")
        class Context_Can_Delete_Product {
            Long productId;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
            }

            @Test
            @DisplayName("제품이 잘 삭제되었는지 확인")
            void deleteProduct() {
                inventoryCommandService.deleteProduct(productId, admin);

                verify(productRepository).deleteById(productId);
            }
        }
    }


    @Nested
    @DisplayName("increaseInventoryView 메소드는")
    class Describe_IncreaseInventoryView {
        @Nested
        @DisplayName("뷰어면")
        class Context_Viewer {
            Long inventoryId;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;

            }

            @Test
            @DisplayName("조회수를 증가할 수 없으므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.increaseInventoryView(inventoryId, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("조회수를 증가할 수 있으면")
        class Context_Can_Increase_View {
            Long inventoryId;
            Inventory inventory;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                inventory = createInventory(null);
                when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));
            }

            @Test
            @DisplayName("조회수를 증가한다.")
            void it_increases_view() {
                inventoryCommandService.increaseInventoryView(inventoryId, admin);

                assertThat(inventory.getView()).isEqualTo(1L);
            }
        }
    }

}
