package com.yellobook.domains.inventory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.dto.request.AddProductRequest;
import com.yellobook.domains.inventory.dto.request.ModifyProductAmountRequest;
import com.yellobook.domains.inventory.dto.response.AddInventoryResponse;
import com.yellobook.domains.inventory.dto.response.AddProductResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsNameResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse;
import com.yellobook.domains.inventory.dto.response.GetSubProductNameResponse;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse;
import com.yellobook.domains.inventory.service.InventoryCommandService;
import com.yellobook.domains.inventory.service.InventoryQueryService;
import java.util.Arrays;
import java.util.Collections;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("InventoryController Unit Test")
class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryQueryService inventoryQueryService;
    @MockBean
    private InventoryCommandService inventoryCommandService;
    @MockBean
    private TeamMemberArgumentResolver teamMemberArgumentResolver;

    private final TeamMemberVO teamMemberVO = TeamMemberVO.of(1L, 1L, MemberTeamRole.ADMIN);

    @BeforeEach
    void setUp() throws Exception {
        when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(teamMemberVO);
    }

    @Nested
    @DisplayName("getTotalInventory 메소드는")
    class Describe_GetTotalInventory {
        @Nested
        @DisplayName("page, size가 1 이상이면")
        class Context_Page_And_Size_GOE_One {
            Integer page;
            Integer size;
            GetTotalInventoryResponse response;

            @BeforeEach
            void setUpContext() {
                page = 1;
                size = 5;
                response = GetTotalInventoryResponse.builder()
                        .page(page)
                        .size(0)
                        .inventories(Collections.emptyList())
                        .build();
                when(inventoryQueryService.getTotalInventory(page, size, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("전체 재고 정보를 반환한다.")
            void it_returns_total_inventory() throws Exception {
                mockMvc.perform(get("/api/v1/inventories")
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.page", CoreMatchers.is(response.page())))
                        .andExpect(jsonPath("$.data.size", CoreMatchers.is(response.size())))
                        .andReturn();
            }
        }

        @Nested
        @DisplayName("page가 1보다 작으면")
        class Context_Page_Less_Than_One {
            Integer page;
            Integer size;
            GetTotalInventoryResponse response;

            @BeforeEach
            void setUpContext() {
                page = 0;
                size = 5;
                response = GetTotalInventoryResponse.builder()
                        .page(page)
                        .size(0)
                        .inventories(Collections.emptyList())
                        .build();
                when(inventoryQueryService.getTotalInventory(page, size, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("상태 코드 400을 반환한다.")
            void it_returns_400() throws Exception {
                mockMvc.perform(get("/api/v1/inventories")
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn();
            }
        }

        @Nested
        @DisplayName("size가 1보다 작으면")
        class Context_Size_Less_Than_One {
            Integer page;
            Integer size;
            GetTotalInventoryResponse response;

            @BeforeEach
            void setUpContext() {
                page = 1;
                size = 0;
                response = GetTotalInventoryResponse.builder()
                        .page(page)
                        .size(0)
                        .inventories(Collections.emptyList())
                        .build();
                when(inventoryQueryService.getTotalInventory(page, size, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("상태 코드 400을 반환한다.")
            void it_returns_400() throws Exception {
                mockMvc.perform(get("/api/v1/inventories")
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("getProductsByInventory 메소드는")
    class Describe_GetProductsByInventory {
        @Nested
        @DisplayName("유효한 재고 Id면")
        class Context_Inventory_Id_Exist {
            Long inventoryId;
            GetProductsResponse response;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                response = GetProductsResponse.builder()
                        .products(Collections.emptyList())
                        .build();
                when(inventoryQueryService.getProductsByInventory(inventoryId, teamMemberVO)).thenReturn(response);
                when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(true);
            }

            @Test
            @DisplayName("해당 재고의 정보를 반환한다.")
            void it_returns_inventory() throws Exception {
                mockMvc.perform(get("/api/v1/inventories/{inventoryId}", inventoryId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.products", CoreMatchers.is(response.products())))
                        .andReturn();
            }
        }

        @Nested
        @DisplayName("유효한 재고 Id가 아니면")
        class Context_Inventory_Id_Not_Exist {
            Long inventoryId;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(false);
            }

            @Test
            @DisplayName("상태 코드 400을 반환한다.")
            void it_returns_400() throws Exception {
                mockMvc.perform(get("/api/v1/inventories/{inventoryId}", inventoryId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("getProductNames 메소드는")
    class Describe_GetProductNames {
        @Nested
        @DisplayName("제품명을 검색하면")
        class Context_Search_Product_Name {
            String name;
            GetProductsNameResponse response;

            @BeforeEach
            void setUpContext() {
                name = "product";
                response = GetProductsNameResponse.builder()
                        .build();
                when(inventoryQueryService.getProductsName(name, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("제품 정보를 반환한다.")
            void it_returns_product() throws Exception {
                mockMvc.perform(get("/api/v1/inventories/products/search")
                                .param("name", name)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.names", CoreMatchers.is(response.names())))
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("getSubProductName 메소드는")
    class Describe_GetSubProductName {
        @Nested
        @DisplayName("제품 이름을 검색하면")
        class Context_Search_Product_Name {
            String name;
            GetSubProductNameResponse response;

            @BeforeEach
            void setUpContext() {
                name = "product";
                response = GetSubProductNameResponse.builder()
                        .build();
                when(inventoryQueryService.getSubProductName(name, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("제품 이름과 동일한 제품들의 하위 제품명을 반환한다.")
            void it_returns_subProduct_name() throws Exception {
                mockMvc.perform(get("/api/v1/inventories/subProducts/search")
                                .param("name", name)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.subProducts", CoreMatchers.is(response.subProducts())))
                        .andReturn();
            }
        }

    }

    @Nested
    @DisplayName("getProductByKeywordAndInventory 메소드는")
    class Describe_GetProductByKeywordAndInventory {
        @Nested
        @DisplayName("유효한 재고 Id 이고, 제품 이름을 검색하면")
        class Context_Inventory_Id_Exist {
            Long inventoryId;
            String keyword;
            GetProductsResponse response;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                keyword = "product";
                response = GetProductsResponse.builder()
                        .products(Collections.emptyList())
                        .build();
                when(inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword,
                        teamMemberVO)).thenReturn(response);
                when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(true);
            }

            @Test
            @DisplayName("제품 이름을 포함하는 제품들을 반환한다.")
            void it_returns_products_contain_keyword() throws Exception {
                mockMvc.perform(get("/api/v1/inventories/{inventoryId}/search", inventoryId)
                                .queryParam("keyword", keyword)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.products", CoreMatchers.is(response.products())))
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("addProduct 메소드는")
    class Describe_AddProduct {
        @Nested
        @DisplayName("유효한 재고 Id면")
        class Context_Inventory_Id_Exist {
            Long inventoryId;
            AddProductRequest request;
            AddProductResponse response;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                request = AddProductRequest.builder()
                        .name("name")
                        .subProduct("sub")
                        .sku(0)
                        .purchasePrice(1)
                        .salePrice(1)
                        .amount(0)
                        .build();
                response = AddProductResponse.builder()
                        .productId(1L)
                        .build();
                when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(true);
                when(inventoryCommandService.addProduct(inventoryId, request, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("해당 재고에 새로운 제품을 추가한다.")
            void it_adds_new_product() throws Exception {
                mockMvc.perform(post("/api/v1/inventories/{inventoryId}", inventoryId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.data.productId", CoreMatchers.is(response.productId()
                                .intValue())))
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("modifyProductAmount 메소드는")
    class Describe_ModifyProductAmount {
        @Nested
        @DisplayName("유효한 제품 Id가 아니면")
        class Context_Product_Id_Not_Exist {
            Long productId;
            ModifyProductAmountRequest request;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
                request = ModifyProductAmountRequest.builder()
                        .build();
                when(inventoryQueryService.existByProductId(productId)).thenReturn(false);
            }

            @Test
            @DisplayName("상태 코드 400을 반환한다.")
            void it_returns_400() throws Exception {
                mockMvc.perform(put("/api/v1/inventories/products/{productId}", productId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn();
            }
        }

        @Nested
        @DisplayName("유효한 제품 Id이면")
        class Context_Product_Id_Exist {
            Long productId;
            ModifyProductAmountRequest request;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
                request = ModifyProductAmountRequest.builder()
                        .amount(100)
                        .build();
                when(inventoryQueryService.existByProductId(productId)).thenReturn(true);
                doNothing().when(inventoryCommandService)
                        .modifyProductAmount(productId, request, teamMemberVO);
            }

            @Test
            @DisplayName("해당 제품 정보를 반환한다.")
            void it_returns_product_info() throws Exception {
                mockMvc.perform(put("/api/v1/inventories/products/{productId}", productId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("deleteProduct 메소드는")
    class Describe_DeleteProduct {
        @Nested
        @DisplayName("유효한 제품 Id면")
        class Context_Product_Id_Exist {
            Long productId;

            @BeforeEach
            void setUpContext() {
                productId = 1L;
                when(inventoryQueryService.existByProductId(productId)).thenReturn(true);
                doNothing().when(inventoryCommandService)
                        .deleteProduct(productId, teamMemberVO);
            }

            @Test
            @DisplayName("해당 제품을 삭제한다.")
            void it_deletes_product() throws Exception {
                mockMvc.perform(delete("/api/v1/inventories/products/{productId}", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("increaseInventoryView 메소드는")
    class Describe_IncreaseInventoryView {
        @Nested
        @DisplayName("유효한 재고 Id면")
        class Context_Inventory_Id_Exist {
            Long inventoryId;

            @BeforeEach
            void setUpContext() {
                inventoryId = 1L;
                when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(true);
                doNothing().when(inventoryCommandService)
                        .increaseInventoryView(inventoryId, teamMemberVO);
            }

            @Test
            @DisplayName("재고 조회수를 증가한다.")
            void it_increases_view() throws Exception {
                mockMvc.perform(patch("/api/v1/inventories/{inventoryId}/views", inventoryId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();
            }
        }
    }

    @Nested
    @DisplayName("addInventory 메소드는")
    class Describe_AddInventory {
        @Nested
        @DisplayName("파일이 주어지면")
        class Context_File_Given {
            MockMultipartFile file;
            AddInventoryResponse response;

            @BeforeEach
            void setUpContext() {
                file = new MockMultipartFile("file", "test.xlsx", MediaType.APPLICATION_OCTET_STREAM_VALUE,
                        "some data".getBytes());
                response = AddInventoryResponse.builder()
                        .inventoryId(1L)
                        .productIds(Arrays.asList(1L, 2L, 3L))
                        .build();
                when(inventoryCommandService.addInventory(file, teamMemberVO)).thenReturn(response);
            }

            @Test
            @DisplayName("상태 코드 201을 반환한다.")
            void it_returns_201() throws Exception {
                mockMvc.perform(multipart("/api/v1/inventories")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.data.inventoryId", CoreMatchers.is(response.inventoryId()
                                .intValue())))
                        .andDo(print())
                        .andReturn();
            }
        }

        @Nested
        @DisplayName("파일이 주어지지 않으면")
        class Context_File_Not_Given {

            @Test
            @DisplayName("상태 코드 400을 반환한다.")
            void it_returns_400() throws Exception {
                mockMvc.perform(multipart("/api/v1/inventories")
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn();
            }
        }
    }

}
