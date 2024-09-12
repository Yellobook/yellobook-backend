package com.yellobook.domains.inventory.service;

import static fixture.InventoryFixture.createInventory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.dto.query.QueryProductName;
import com.yellobook.domains.inventory.dto.query.QuerySubProduct;
import com.yellobook.domains.inventory.dto.response.GetProductsNameResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse.ProductInfo;
import com.yellobook.domains.inventory.dto.response.GetSubProductNameResponse;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse.InventoryInfo;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.mapper.InventoryMapper;
import com.yellobook.domains.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.exception.CustomException;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@ExtendWith(MockitoExtension.class)
@DisplayName("InventoryQueryService Unit Test")
class InventoryQueryServiceTest {
    @InjectMocks
    private InventoryQueryService inventoryQueryService;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private InventoryMapper inventoryMapper;
    @Mock
    private ProductMapper productMapper;

    private final TeamMemberVO admin = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);
    private final TeamMemberVO orderer = TeamMemberVO.of(2L, 1L, MemberTeamRole.ORDERER);
    private final TeamMemberVO viewer = TeamMemberVO.of(3L, 1L, MemberTeamRole.VIEWER);

    @Nested
    @DisplayName("getTotalInventory 메소드는")
    class Describe_GetTotalInventory {
        @Nested
        @DisplayName("뷰어라면")
        class Context_Viewer {
            Integer page;
            Integer size;

            @BeforeEach
            void setUpContext() {
                page = 1;
                size = 1;
            }

            @Test
            @DisplayName("전체 재고 현황 확인이 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryQueryService.getTotalInventory(page, size, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("재고가 존재하면")
        class Context_Inventory_Exist {
            Integer page;
            Integer size;
            Pageable pageable;
            List<QueryInventory> queryInventories;
            List<InventoryInfo> inventoryInfos;
            GetTotalInventoryResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                page = 1;
                size = 5;
                pageable = PageRequest.of(page - 1, size);
                queryInventories = createInventoryDTOs();
                inventoryInfos = createInventoryInfo();
                expectResponse = GetTotalInventoryResponse.builder()
                        .page(page)
                        .size(inventoryInfos.size())
                        .inventories(inventoryInfos)
                        .build();
                when(inventoryRepository.getTotalInventory(admin.getTeamId(), pageable)).thenReturn(queryInventories);
                when(inventoryMapper.toInventoryInfoList(queryInventories)).thenReturn(inventoryInfos);
                when(inventoryMapper.toGetTotalInventoryResponse(page, inventoryInfos.size(),
                        inventoryInfos)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("전체 재고를 조회한다.")
            void it_returns_total_inventory() {
                GetTotalInventoryResponse response = inventoryQueryService.getTotalInventory(page, size, admin);

                verify(inventoryRepository).getTotalInventory(admin.getTeamId(), pageable);
                verify(inventoryMapper).toInventoryInfoList(queryInventories);
                assertThat(response.page()).isEqualTo(expectResponse.page());
                assertThat(response.size()).isEqualTo(expectResponse.size());
                assertThat(response.inventories()).isSameAs(inventoryInfos);
            }
        }

        @Nested
        @DisplayName("재고가 없으면")
        class Context_Empty_Inventory {
            Integer page;
            Integer size;
            Pageable pageable;
            List<QueryInventory> queryInventories;
            List<InventoryInfo> inventoryInfos;
            GetTotalInventoryResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                page = 1;
                size = 5;
                pageable = PageRequest.of(page - 1, size);
                queryInventories = Collections.emptyList();
                inventoryInfos = Collections.emptyList();
                expectResponse = GetTotalInventoryResponse.builder()
                        .page(page)
                        .size(0)
                        .inventories(Collections.emptyList())
                        .build();
                when(inventoryRepository.getTotalInventory(admin.getTeamId(), pageable)).thenReturn(queryInventories);
                when(inventoryMapper.toInventoryInfoList(queryInventories)).thenReturn(inventoryInfos);
                when(inventoryMapper.toGetTotalInventoryResponse(page, 0, inventoryInfos)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                GetTotalInventoryResponse response = inventoryQueryService.getTotalInventory(page, size, admin);

                verify(inventoryRepository).getTotalInventory(admin.getTeamId(), pageable);
                verify(inventoryMapper).toInventoryInfoList(queryInventories);
                assertThat(response.page()).isEqualTo(expectResponse.page());
                assertThat(response.size()).isEqualTo(expectResponse.size());
                assertThat(response.inventories()).isSameAs(Collections.emptyList());
            }
        }

        private List<QueryInventory> createInventoryDTOs() {
            List<QueryInventory> result = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                result.add(QueryInventory.builder()
                        .build());
            }
            return result;
        }

        private List<InventoryInfo> createInventoryInfo() {
            List<InventoryInfo> result = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                result.add(InventoryInfo.builder()
                        .build());
            }
            return result;
        }

    }

    @Nested
    @DisplayName("getProductsByInventory 메소드는")
    class Describe_GetProductsByInventory {
        @Nested
        @DisplayName("뷰어라면")
        class Context_Viewer {
            Long inventoryId = 1L;

            @Test
            @DisplayName("전체 재고 현황 확인 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryQueryService.getProductsByInventory(inventoryId, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("재고에 제품이 존재하면")
        class Context_Product_Exist {
            Long inventoryId;
            List<QueryProduct> queryProducts;
            GetProductsResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                queryProducts = createProductDTOs();
                expectResponse = createGetProductsResponse();
                when(productRepository.getProducts(inventoryId)).thenReturn(queryProducts);
                when(productMapper.toGetProductsResponse(queryProducts)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("제품들을 반환한다.")
            void it_returns_products() {
                GetProductsResponse response = inventoryQueryService.getProductsByInventory(inventoryId, admin);

                verify(productRepository).getProducts(inventoryId);
                assertThat(response.products()).isSameAs(expectResponse.products());
            }
        }

        @Nested
        @DisplayName("재고에 제품이 없으면")
        class Context_Empty_Product {
            Long inventoryId;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                List<QueryProduct> queryProducts = Collections.emptyList();
                GetProductsResponse expectResponse = GetProductsResponse.builder()
                        .products(Collections.emptyList())
                        .build();
                when(productRepository.getProducts(inventoryId)).thenReturn(queryProducts);
                when(productMapper.toGetProductsResponse(queryProducts)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                GetProductsResponse response = inventoryQueryService.getProductsByInventory(inventoryId, admin);

                verify(productRepository).getProducts(inventoryId);
                assertThat(response.products()).isSameAs(Collections.emptyList());
            }
        }

    }

    @Nested
    @DisplayName("getProductByKeywordAndInventory 메소드는")
    class Describe_GetProductByKeywordAndInventory {
        @Nested
        @DisplayName("주문자라면")
        class Context_Orderer {
            Long inventoryId;
            String keyword;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                keyword = "pro";
            }

            @Test
            @DisplayName("재고 검색 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, orderer));
                Assertions.assertEquals(AuthErrorCode.ORDERER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("뷰어라면")
        class Context_Viewer {
            Long inventoryId;
            String keyword;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                keyword = "pro";
            }

            @Test
            @DisplayName("재고 검색 불가능하므로 예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, viewer));
                Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("검색 키워드를 제품명에 포함하는 제품이 존재하면")
        class Context_Product_Contain_Keyword_Exist {
            Long inventoryId;
            String keyword;
            GetProductsResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                keyword = "pro";
                List<QueryProduct> queryProducts = createProductDTOs();
                expectResponse = createGetProductsResponse();
                when(productRepository.getProducts(inventoryId, keyword)).thenReturn(queryProducts);
                when(productMapper.toGetProductsResponse(queryProducts)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("제품을 반환한다.")
            void it_returns_product() {
                GetProductsResponse response = inventoryQueryService.getProductByKeywordAndInventory(inventoryId,
                        keyword, admin);

                verify(productRepository).getProducts(inventoryId, keyword);
                assertThat(response.products()).isSameAs(expectResponse.products());
            }
        }

        @Nested
        @DisplayName("검색 키워드를 제품명에 포함하는 제품이 존재하지 않으면")
        class Context_Product_Contain_Keyword_Not_Exist {
            Long inventoryId;
            String keyword;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                keyword = "pro";
                List<QueryProduct> queryProducts = Collections.emptyList();
                when(productRepository.getProducts(inventoryId, keyword)).thenReturn(Collections.emptyList());
                when(productMapper.toGetProductsResponse(queryProducts)).thenReturn(GetProductsResponse.builder()
                        .products(Collections.emptyList())
                        .build());
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                GetProductsResponse response = inventoryQueryService.getProductByKeywordAndInventory(inventoryId,
                        keyword, admin);

                verify(productRepository).getProducts(inventoryId, keyword);
                assertThat(response.products()).isSameAs(Collections.emptyList());
            }
        }
    }

    @Nested
    @DisplayName("getProductsName 메소드는")
    class Describe_GetProductsName {
        @Nested
        @DisplayName("재고가 존재하지 않으면")
        class Context_Inventory_Not_Exist {
            String name;

            @BeforeEach
            void setUpContext() {
                name = "product";
                GetProductsNameResponse expectResult = GetProductsNameResponse.builder()
                        .names(Collections.emptyList())
                        .build();
                when(inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId())).thenReturn(
                        Optional.empty());
                when(productMapper.toEmptyGetProductNameResponse()).thenReturn(expectResult);

            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                GetProductsNameResponse response = inventoryQueryService.getProductsName(name, admin);

                assertThat(response.names()).isEqualTo(Collections.emptyList());
                verify(inventoryRepository).findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId());
                verify(productMapper).toEmptyGetProductNameResponse();
            }
        }

        @Nested
        @DisplayName("재고가 존재하면")
        class Context_Inventory_Exist {
            String name;
            Inventory inventory;
            List<QueryProductName> productsName;
            GetProductsNameResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                name = "product";
                inventory = createInventoryWithId();
                productsName = Collections.emptyList();
                expectResponse = GetProductsNameResponse.builder()
                        .names(Collections.emptyList())
                        .build();
                when(inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId())).thenReturn(
                        Optional.of(inventory));
                when(productRepository.getProductsName(inventory.getId(), name)).thenReturn(productsName);
                when(productMapper.toGetProductsNameResponse(productsName)).thenReturn(expectResponse);
            }

            @Test
            @DisplayName("검색 키워드를 포함하고 있는 제품 이름을 반환한다.")
            void it_returns_products_name_contain_keyword() {
                GetProductsNameResponse response = inventoryQueryService.getProductsName(name, admin);

                assertThat(response).isNotNull();
                assertThat(response.names()).isEqualTo(expectResponse.names());
                verify(inventoryRepository).findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId());
                verify(productRepository).getProductsName(inventory.getId(), name);
                verify(productMapper).toGetProductsNameResponse(productsName);
            }
        }

    }

    @Nested
    @DisplayName("getSubProductName 메소드는")
    class Describe_GetSubProductName {
        @Nested
        @DisplayName("재고가 존재하지 않으면")
        class Context_Inventory_Not_Exist {
            String name;

            @BeforeEach
            void setUpContext() {
                name = "product";
                GetSubProductNameResponse expectResult = GetSubProductNameResponse.builder()
                        .subProducts(Collections.emptyList())
                        .build();
                when(inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId())).thenReturn(
                        Optional.empty());
                when(productMapper.toEmptyGetSubProductNameResponse()).thenReturn(expectResult);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                GetSubProductNameResponse response = inventoryQueryService.getSubProductName(name, admin);

                assertThat(response.subProducts()).isEqualTo(Collections.emptyList());
                verify(inventoryRepository).findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId());
                verify(productMapper).toEmptyGetSubProductNameResponse();
            }
        }

        @Nested
        @DisplayName("재고가 존재하면")
        class Context_Inventory_Exist {
            String name;
            Inventory inventory;
            List<QuerySubProduct> subProducts;
            GetSubProductNameResponse expectResponse;

            @BeforeEach
            void setUpContext() {
                name = "product";
                inventory = createInventoryWithId();
                subProducts = createQuerySubProducts();
                expectResponse = createGetSubProductNameResponse();
                when(inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId())).thenReturn(
                        Optional.of(inventory));
                when(productRepository.getSubProducts(inventory.getId(), name)).thenReturn(subProducts);
                when(productMapper.toGetSubProductNameResponse(subProducts)).thenReturn(expectResponse);

            }

            @Test
            @DisplayName("제품 이름과 일치하는 제품의 정보를 반환한다.")
            void it_returns_products_match_product_name() {
                GetSubProductNameResponse response = inventoryQueryService.getSubProductName(name, admin);

                assertThat(response).isNotNull();
                assertThat(response.subProducts()).isEqualTo(expectResponse.subProducts());
                verify(inventoryRepository).findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId());
                verify(productRepository).getSubProducts(inventory.getId(), name);
                verify(productMapper).toGetSubProductNameResponse(subProducts);
            }
        }

        private List<QuerySubProduct> createQuerySubProducts() {
            return List.of(
                    QuerySubProduct.builder()
                            .productId(1L)
                            .subProductName("SubProduct A")
                            .build(),
                    QuerySubProduct.builder()
                            .productId(2L)
                            .subProductName("SubProduct B")
                            .build(),
                    QuerySubProduct.builder()
                            .productId(3L)
                            .subProductName("SubProduct C")
                            .build()
            );
        }

        private GetSubProductNameResponse createGetSubProductNameResponse() {
            List<GetSubProductNameResponse.SubProductInfo> subProductInfos = List.of(
                    GetSubProductNameResponse.SubProductInfo.builder()
                            .productId(1L)
                            .subProductName("SubProduct A")
                            .build(),
                    GetSubProductNameResponse.SubProductInfo.builder()
                            .productId(2L)
                            .subProductName("SubProduct B")
                            .build(),
                    GetSubProductNameResponse.SubProductInfo.builder()
                            .productId(3L)
                            .subProductName("SubProduct C")
                            .build()
            );

            return GetSubProductNameResponse.builder()
                    .subProducts(subProductInfos)
                    .build();
        }
    }

    private List<QueryProduct> createProductDTOs() {
        List<QueryProduct> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            result.add(QueryProduct.builder()
                    .name("product")
                    .build());
        }
        return result;
    }

    private GetProductsResponse createGetProductsResponse() {
        List<ProductInfo> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            result.add(ProductInfo.builder()
                    .name("product")
                    .build());
        }
        return GetProductsResponse.builder()
                .products(result)
                .build();
    }

    private Inventory createInventoryWithId() {
        Inventory inventory = createInventory(null);
        try {
            Field idField = Inventory.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(inventory, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set order comment ID", e);
        }
        return inventory;
    }

}
