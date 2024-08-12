package com.yellobook.domains.inventory.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.dto.query.QueryProductName;
import com.yellobook.domains.inventory.dto.query.QuerySubProduct;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse.ProductInfo;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse.InventoryInfo;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.mapper.InventoryMapper;
import com.yellobook.domains.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
import com.yellobook.domains.inventory.dto.response.GetProductsNameResponse;
import com.yellobook.domains.inventory.dto.response.GetSubProductNameResponse;
import com.yellobook.error.code.AuthErrorCode;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
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
    @DisplayName("전체 재고 현황 확인")
    class GetTotalInventoryTests{
        @Test
        @DisplayName("뷰어는 전체 재고 현황 확인 불가능")
        void viewCantGetTotalInventory(){
            //given
            Integer page = 1;
            Integer size = 1;

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    inventoryQueryService.getTotalInventory(page, size, viewer));
            Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
        }

        @Test
        @DisplayName("제고가 존재하면 전체 재고 조회")
        void getTotalInventory(){
            //given
            Integer page = 1;
            Integer size = 5;
            Pageable pageable = PageRequest.of(page-1, size);
            List<QueryInventory> queryInventories = createInventoryDTOs();
            List<InventoryInfo> inventoryInfos = createInventoryInfo();
            when(inventoryRepository.getTotalInventory(admin.getTeamId(), pageable)).thenReturn(queryInventories);
            when(inventoryMapper.toInventoryInfoList(queryInventories)).thenReturn(inventoryInfos);

            //when
            GetTotalInventoryResponse response = inventoryQueryService.getTotalInventory(page, size, admin);

            //then
            verify(inventoryRepository).getTotalInventory(admin.getTeamId(), pageable);
            verify(inventoryMapper).toInventoryInfoList(queryInventories);
            assertThat(response.page()).isEqualTo(page);
            assertThat(response.size()).isEqualTo(4);
            assertThat(response.inventories()).isSameAs(inventoryInfos);
        }

        @Test
        @DisplayName("제고가 없으면 빈 리스트를 반환한다.")
        void getEmptyInventory(){
            //given
            Integer page = 1;
            Integer size = 5;
            Pageable pageable = PageRequest.of(page-1, size);
            List<QueryInventory> queryInventories = Collections.emptyList();
            List<InventoryInfo> inventoryInfos = Collections.emptyList();
            when(inventoryRepository.getTotalInventory(admin.getTeamId(), pageable)).thenReturn(queryInventories);
            when(inventoryMapper.toInventoryInfoList(queryInventories)).thenReturn(inventoryInfos);

            //when
            GetTotalInventoryResponse response = inventoryQueryService.getTotalInventory(page, size, admin);

            //then
            verify(inventoryRepository).getTotalInventory(admin.getTeamId(), pageable);
            verify(inventoryMapper).toInventoryInfoList(queryInventories);
            assertThat(response.page()).isEqualTo(page);
            assertThat(response.size()).isEqualTo(0);
            assertThat(response.inventories()).isSameAs(Collections.emptyList());
        }

        private List<QueryInventory> createInventoryDTOs(){
            List<QueryInventory> result = new ArrayList<>();
            for(int i =0; i<4; i++){
                result.add(QueryInventory.builder().build());
            }
            return result;
        }

        private List<InventoryInfo> createInventoryInfo(){
            List<InventoryInfo> result = new ArrayList<>();
            for(int i =0; i<4;i++){
                result.add(InventoryInfo.builder().build());
            }
            return result;
        }

    }

    @Nested
    @DisplayName("특정 재고 일자 현황 조회")
    class GetProductsByInventoryTests{
        @Test
        @DisplayName("뷰어는 전체 재고 현황 확인 불가능")
        void viewerCantGetProducts(){
            //given
            Long inventoryId = 1L;

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    inventoryQueryService.getProductsByInventory(inventoryId, viewer));
            Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
        }

        @Test
        @DisplayName("재고를 가져오기 확인")
        void checkOrderByName(){
            //given
            Long inventoryId = 1L;
            List<QueryProduct> queryProducts = createProductDTOs();
            List<ProductInfo> productInfos = createProductInfos();
            when(productRepository.getProducts(inventoryId)).thenReturn(queryProducts);
            when(productMapper.toProductInfo(queryProducts)).thenReturn(productInfos);

            //when
            GetProductsResponse response = inventoryQueryService.getProductsByInventory(inventoryId, admin);

            //then
            verify(productRepository).getProducts(inventoryId);
            verify(productMapper).toProductInfo(queryProducts);
            assertThat(response.products()).isSameAs(productInfos);
        }

        @Test
        @DisplayName("재고가 없으면 빈 리스트 반환")
        void getEmptyListProducts(){
            //given
            Long inventoryId = 1L;
            List<QueryProduct> queryProducts = Collections.emptyList();
            List<ProductInfo> productInfos = Collections.emptyList();
            when(productRepository.getProducts(inventoryId)).thenReturn(queryProducts);
            when(productMapper.toProductInfo(queryProducts)).thenReturn(productInfos);

            //when
            GetProductsResponse response = inventoryQueryService.getProductsByInventory(inventoryId, admin);

            //then
            verify(productRepository).getProducts(inventoryId);
            verify(productMapper).toProductInfo(queryProducts);
            assertThat(response.products()).isSameAs(Collections.emptyList());
        }

    }

    @Nested
    @DisplayName("재고 검색")
    class GetProductByKeywordAndInventoryTests{
        @Test
        @DisplayName("주문자는 재고 검색 불가능")
        void ordererCantGetProducts(){
            //given
            Long inventoryId = 1L;
            String keyword = "pro";

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, orderer));
            Assertions.assertEquals(AuthErrorCode.ORDERER_NOT_ALLOWED, exception.getErrorCode());
        }

        @Test
        @DisplayName("뷰어는 재고 검색 불가능")
        void viewerCantGetProducts(){
            //given
            Long inventoryId = 1L;
            String keyword = "pro";

            //when & then
            CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                    inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, viewer));
            Assertions.assertEquals(AuthErrorCode.VIEWER_NOT_ALLOWED, exception.getErrorCode());
        }

        @Test
        @DisplayName("재고 검색 확인")
        void getProducts(){
            //given
            Long inventoryId = 1L;
            String keyword = "pro";
            List<QueryProduct> queryProducts = createProductDTOs();
            List<ProductInfo> productInfos = createProductInfos();
            when(productRepository.getProducts(inventoryId, keyword)).thenReturn(queryProducts);
            when(productMapper.toProductInfo(queryProducts)).thenReturn(productInfos);

            //when
            GetProductsResponse response = inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, admin);

            //then
            verify(productRepository).getProducts(inventoryId, keyword);
            verify(productMapper).toProductInfo(queryProducts);
            assertThat(response.products()).isSameAs(productInfos);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 리스트 반환")
        void getEmptyProducts(){
            //given
            Long inventoryId = 1L;
            String keyword = "pro";
            List<QueryProduct> queryProducts = Collections.emptyList();
            List<ProductInfo> productInfos = Collections.emptyList();
            when(productRepository.getProducts(inventoryId, keyword)).thenReturn(Collections.emptyList());
            when(productMapper.toProductInfo(queryProducts)).thenReturn(productInfos);

            //when
            GetProductsResponse response = inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, admin);

            //then
            verify(productRepository).getProducts(inventoryId, keyword);
            verify(productMapper).toProductInfo(queryProducts);
            assertThat(response.products()).isSameAs(Collections.emptyList());
        }

    }

    @Nested
    @DisplayName("제품 이름 검색")
    class GetProductsNameTest{
        @Test
        @DisplayName("team에 제고가 존재하지 않으면 빈 리스트 반환")
        void getEmptyProductName(){
            //given
            String name = "product";
            GetProductsNameResponse expectResult = GetProductsNameResponse.builder().names(Collections.emptyList()).build();
            when(inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId())).thenReturn(Optional.empty());
            when(productMapper.toEmptyGetProductNameResponse()).thenReturn(expectResult);

            //when
            GetProductsNameResponse response = inventoryQueryService.getProductsName(name, admin);

            //then
            assertThat(response.names()).isEqualTo(Collections.emptyList());
            verify(inventoryRepository).findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId());
            verify(productMapper).toEmptyGetProductNameResponse();
        }

        @Test
        @DisplayName("재고가 존재하면 해당 재고에 있는 제품 중 검색 키워드를 포함하고 있는 제품 이름 반환")
        void getProductName(){
            //given
            String name = "product";
            Inventory inventory = createInventory();
            List<QueryProductName> productsName = Collections.emptyList();
            GetProductsNameResponse expectResponse = GetProductsNameResponse.builder().names(Collections.emptyList()).build();
            when(inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId())).thenReturn(Optional.of(inventory));
            when(productRepository.getProductsName(inventory.getId(), name)).thenReturn(productsName);
            when(productMapper.toGetProductsNameResponse(productsName)).thenReturn(expectResponse);

            //when
            GetProductsNameResponse response = inventoryQueryService.getProductsName(name, admin);

            //then
            assertThat(response).isNotNull();
            assertThat(response.names()).isEqualTo(expectResponse.names());
            verify(inventoryRepository).findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId());
            verify(productRepository).getProductsName(inventory.getId(), name);
            verify(productMapper).toGetProductsNameResponse(productsName);
        }

    }

    @Nested
    @DisplayName("제품 이름으로 하위 제품 조회")
    class GetSubProductNameTest{
        @Test
        @DisplayName("team에 제고가 존재하지 않으면 빈 리스트 반환")
        void getEmptySubProductName(){
            //given
            String name = "product";
            GetSubProductNameResponse expectResult = GetSubProductNameResponse.builder().subProducts(Collections.emptyList()).build();
            when(inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId())).thenReturn(Optional.empty());
            when(productMapper.toEmptyGetSubProductNameResponse()).thenReturn(expectResult);

            //when
            GetSubProductNameResponse response = inventoryQueryService.getSubProductName(name, admin);

            //then
            assertThat(response.subProducts()).isEqualTo(Collections.emptyList());
            verify(inventoryRepository).findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId());
            verify(productMapper).toEmptyGetSubProductNameResponse();
        }

        @Test
        @DisplayName("재고가 존재하면 해당 재고에 있는 제품 중 제품 이름과 일치하는 제품의 하위 제품 이름과 Id 반환")
        void getSubProductName(){
            //given
            String name = "product";
            Inventory inventory = createInventory();
            List<QuerySubProduct> subProducts = createQuerySubProducts();
            GetSubProductNameResponse expectResponse = createGetSubProductNameResponse();
            when(inventoryRepository.findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId())).thenReturn(Optional.of(inventory));
            when(productRepository.getSubProducts(inventory.getId(), name)).thenReturn(subProducts);
            when(productMapper.toGetSubProductNameResponse(subProducts)).thenReturn(expectResponse);

            //when
            GetSubProductNameResponse response = inventoryQueryService.getSubProductName(name, admin);

            //then
            assertThat(response).isNotNull();
            assertThat(response.subProducts()).isEqualTo(expectResponse.subProducts());
            verify(inventoryRepository).findFirstByTeamIdOrderByCreatedAtDesc(admin.getTeamId());
            verify(productRepository).getSubProducts(inventory.getId(), name);
            verify(productMapper).toGetSubProductNameResponse(subProducts);
        }

        private List<QuerySubProduct> createQuerySubProducts(){
            return List.of(
                    QuerySubProduct.builder().productId(1L).subProductName("SubProduct A").build(),
                    QuerySubProduct.builder().productId(2L).subProductName("SubProduct B").build(),
                    QuerySubProduct.builder().productId(3L).subProductName("SubProduct C").build()
            );
        }

        private GetSubProductNameResponse createGetSubProductNameResponse(){
            List<GetSubProductNameResponse.SubProductInfo> subProductInfos = List.of(
                    GetSubProductNameResponse.SubProductInfo.builder().productId(1L).subProductName("SubProduct A").build(),
                    GetSubProductNameResponse.SubProductInfo.builder().productId(2L).subProductName("SubProduct B").build(),
                    GetSubProductNameResponse.SubProductInfo.builder().productId(3L).subProductName("SubProduct C").build()
            );

            return GetSubProductNameResponse.builder()
                    .subProducts(subProductInfos)
                    .build();
        }

    }


    private List<QueryProduct> createProductDTOs(){
        List<QueryProduct> result = new ArrayList<>();
        for(int i =0; i<5; i++){
            result.add(QueryProduct.builder().name("product").build());
        }
        return result;
    }

    private List<ProductInfo> createProductInfos(){
        List<ProductInfo> result = new ArrayList<>();
        for(int i =0; i<5; i++){
            result.add(ProductInfo.builder().name("product").build());
        }
        return result;
    }

    private Inventory createInventory(){
        Inventory inventory = Inventory.builder().build();
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
