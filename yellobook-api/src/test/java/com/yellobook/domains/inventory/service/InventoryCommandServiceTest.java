package com.yellobook.domains.inventory.service;

import static fixture.InventoryFixture.createInventory;
import static fixture.InventoryFixture.createProduct;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static support.ReflectionUtil.setField;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.dto.cond.ExcelProductCond;
import com.yellobook.domains.inventory.dto.request.AddProductRequest;
import com.yellobook.domains.inventory.dto.request.ModifyProductAmountRequest;
import com.yellobook.domains.inventory.dto.response.AddInventoryResponse;
import com.yellobook.domains.inventory.dto.response.AddProductResponse;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.domains.inventory.mapper.InventoryMapper;
import com.yellobook.domains.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.domains.inventory.utils.ExcelReadUtil;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.code.FileErrorCode;
import com.yellobook.error.code.InventoryErrorCode;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import fixture.InventoryFixture;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;


@ExtendWith(MockitoExtension.class)
@DisplayName("InventoryCommandService Unit Test")
class InventoryCommandServiceTest {
    private final TeamMemberVO admin = TeamMemberVO.of(1L, 1L, TeamMemberRole.ADMIN);
    private final TeamMemberVO orderer = TeamMemberVO.of(2L, 1L, TeamMemberRole.ORDERER);
    private final TeamMemberVO viewer = TeamMemberVO.of(3L, 1L, TeamMemberRole.VIEWER);
    @InjectMocks
    private InventoryCommandService inventoryCommandService;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private InventoryMapper inventoryMapper;
    @Mock
    private ExcelReadUtil excelReadUtil;

    @Nested
    @DisplayName("addProduct 메소드는")
    class Describe_AddProduct {
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
            Product product;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                request = createAddProductRequest();
                Inventory inventory = createInventory(null);
                setField(inventory, "id", inventoryId);
                product = createProduct(inventory);
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

            @Test
            @DisplayName("재고현황의 updatedAt이 변경되었는지 확인한다.")
            void it_update_updatedAt() {
                inventoryCommandService.addProduct(inventoryId, request, admin);

                verify(inventoryRepository).updateUpdatedAt(eq(product.getInventory()
                        .getId()), any(LocalDateTime.class));
            }

        }

    }

    @Nested
    @DisplayName("modifyProductAmount 메소드는")
    class Describe_ModifyProductAmount {
        private ModifyProductAmountRequest createModifyProductAmountRequest() {
            return ModifyProductAmountRequest.builder()
                    .amount(1000)
                    .build();
        }

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
                Inventory inventory = createInventory(null);
                setField(inventory, "id", 1L);
                product = createProduct(inventory);
                when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            }

            @Test
            @DisplayName("제품 수량 수정이 잘 되었는지 확인한다.")
            void it_modify_product_amount() {
                inventoryCommandService.modifyProductAmount(productId, request, admin);

                assertThat(product.getAmount()).isEqualTo(request.amount());
            }

            @Test
            @DisplayName("재고현황의 updatedAt이 변경되었는지 확인한다.")
            void it_update_updatedAt() {
                inventoryCommandService.modifyProductAmount(productId, request, admin);

                verify(inventoryRepository).updateUpdatedAt(eq(product.getInventory()
                        .getId()), any(LocalDateTime.class));
            }
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
            Product product;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
                Inventory inventory = createInventory(null);
                setField(inventory, "id", 1L);
                product = createProduct(inventory);
                setField(product, "id", 1L);

                when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            }

            @Test
            @DisplayName("제품이 잘 삭제되었는지 확인한다.")
            void it_delete_Product() {
                inventoryCommandService.deleteProduct(productId, admin);

                verify(productRepository).deleteById(productId);
            }

            @Test
            @DisplayName("재고현황의 updatedAt이 변경되었는지 확인한다.")
            void it_update_updatedAt() {
                inventoryCommandService.deleteProduct(productId, admin);

                verify(inventoryRepository).updateUpdatedAt(eq(product.getInventory()
                        .getId()), any(LocalDateTime.class));
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
                setField(inventory, "id", inventoryId);
                when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));
            }

            @Test
            @DisplayName("조회수를 증가한다.")
            void it_increases_view() {
                inventoryCommandService.increaseInventoryView(inventoryId, admin);

                verify(inventoryRepository).increaseView(inventory.getId());
            }
        }
    }

    @Nested
    @DisplayName("addInventory 메소드는")
    class Describe_AddInventory {
        private List<ExcelProductCond> createProductList() {
            return List.of(
                    ExcelProductCond.builder()
                            .name("상품 A")
                            .subProduct("서브 A1")
                            .sku(1001)
                            .purchasePrice(5000)
                            .salePrice(7000)
                            .amount(100)
                            .build()
            );
        }

        @Nested
        @DisplayName("주문자면")
        class Context_Orderer {
            MultipartFile file;

            @BeforeEach
            void setUpContext() {
                file = mock(MultipartFile.class);
            }

            @Test
            @DisplayName("재고를 추가할 수 없으므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.addInventory(file, orderer));
                Assertions.assertEquals(AuthErrorCode.ORDERER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("뷰어면")
        class Context_Viewer {
            MultipartFile file;

            @BeforeEach
            void setUpContext() {
                file = mock(MultipartFile.class);
            }

            @Test
            @DisplayName("재고를 추가할 수 없으므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.addInventory(file, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("파일 IO에 실패하면")
        class Context_File_IO_Fail {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws java.io.IOException {
                file = mock(MultipartFile.class);
                when(excelReadUtil.read(file)).thenThrow(new IOException());
            }

            @Test
            @DisplayName("재고를 추가할 수 없으므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.addInventory(file, admin));
                Assertions.assertEquals(FileErrorCode.FILE_IO_FAIL, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("팀 스페이스가 존재하지 않으면")
        class Context_Team_Not_Exist {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                file = mock(MultipartFile.class);
                Long teamId = 1L;
                List<ExcelProductCond> productConds = Collections.emptyList();
                when(excelReadUtil.read(file)).thenReturn(productConds);
                when(teamRepository.findById(teamId)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("재고를 추가할 수 없으므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryCommandService.addInventory(file, admin));
                Assertions.assertEquals(TeamErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("제품 정보가 제공되면")
        class Context_Product_Info_Given {
            MultipartFile file;
            AddInventoryResponse expectResponse;

            @BeforeEach
            void setUpContext() throws IOException {
                file = mock(MultipartFile.class);

                Long teamId = 1L;
                Team team = createTeam("팀1");
                List<ExcelProductCond> productConds = createProductList();
                when(excelReadUtil.read(file)).thenReturn(productConds);
                when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

                String inventoryTitle = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) + " 재고현황";
                Inventory newInventory = InventoryFixture.createInventory(inventoryTitle, team);
                when(inventoryMapper.toInventory(team, inventoryTitle)).thenReturn(newInventory);
                when(inventoryRepository.save(newInventory)).thenAnswer(invocation -> {
                    Inventory savedInventory = invocation.getArgument(0);
                    Field idField = Inventory.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(savedInventory, 1L); // ID를 주입
                    return savedInventory;
                });

                Product newProduct = InventoryFixture.createProduct(newInventory);
                when(productMapper.toProduct(any(ExcelProductCond.class), any())).thenReturn(newProduct);
                when(productRepository.save(newProduct)).thenAnswer(invocation -> {
                    Product savedProduct = invocation.getArgument(0);
                    Field idField = Product.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(savedProduct, 1L);
                    return savedProduct;
                });

                expectResponse = AddInventoryResponse.builder()
                        .inventoryId(1L)
                        .productIds(List.of(1L))
                        .build();
                when(inventoryMapper.toAddInventoryResponse(anyLong(), anyList())).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("재고와 제품을 저장한다.")
            void it_save_inventory_and_product() {
                AddInventoryResponse response = inventoryCommandService.addInventory(file, admin);

                assertThat(response.inventoryId()).isEqualTo(expectResponse.inventoryId());
                assertThat(1).isEqualTo(expectResponse.productIds()
                        .size());
            }
        }
    }

}
