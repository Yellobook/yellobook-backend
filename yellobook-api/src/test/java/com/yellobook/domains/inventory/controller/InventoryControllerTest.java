package com.yellobook.domains.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.inventory.dto.request.AddProductRequest;
import com.yellobook.domains.inventory.dto.request.ModifyProductAmountRequest;
import com.yellobook.domains.inventory.dto.response.AddProductResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse;
import com.yellobook.domains.inventory.service.InventoryCommandService;
import com.yellobook.domains.inventory.service.InventoryQueryService;
import com.yellobook.domains.inventory.dto.response.GetProductsNameResponse;
import com.yellobook.domains.inventory.dto.response.GetSubProductNameResponse;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
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
    void setTeamMemberVO() throws Exception{
        when(teamMemberArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(teamMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(teamMemberVO);
    }

    @Test
    @DisplayName("전체 재고 현황 글 조회")
    void getTotalInventory() throws Exception{
        //given
        Integer page = 1;
        Integer size = 5;
        GetTotalInventoryResponse response = GetTotalInventoryResponse.builder().page(page).size(0)
                .inventories(Collections.emptyList()).build();
        when(inventoryQueryService.getTotalInventory(page, size, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/inventories")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page", CoreMatchers.is(response.page())))
                .andExpect(jsonPath("$.data.size", CoreMatchers.is(response.size())))
                .andReturn();
    }

    @Test
    @DisplayName("전체 재고 현황 글 조회 - page가 1보다 작을 경우 예외 발생")
    void getTotalInventoryPageLessOne() throws Exception{
        //given
        Integer page = 0;
        Integer size = 5;
        GetTotalInventoryResponse response = GetTotalInventoryResponse.builder().page(page).size(0)
                .inventories(Collections.emptyList()).build();
        when(inventoryQueryService.getTotalInventory(page, size, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/inventories")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("전체 재고 현황 글 조회 - page가 1보다 작을 경우 예외 발생")
    void getTotalInventorySizeLessOne() throws Exception{
        //given
        Integer page = 1;
        Integer size = 0;
        GetTotalInventoryResponse response = GetTotalInventoryResponse.builder().page(page).size(0)
                .inventories(Collections.emptyList()).build();
        when(inventoryQueryService.getTotalInventory(page, size, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/inventories")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("일별 재고 현황 상세 조회 - 재고가 존재하는 경우")
    void getProductsByInventory() throws Exception{
        //given
        Long inventoryId = 1L;
        GetProductsResponse response = GetProductsResponse.builder().products(Collections.emptyList()).build();
        when(inventoryQueryService.getProductsByInventory(inventoryId, teamMemberVO)).thenReturn(response);
        when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(true);

        //when & then
        mockMvc.perform(get("/api/v1/inventories/{inventoryId}", inventoryId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.products", CoreMatchers.is(response.products())))
                .andReturn();
    }

    @Test
    @DisplayName("제품 이름으로 제품 조회")
    void getProductNames() throws Exception{
        //given
        String name = "product";
        GetProductsNameResponse response = GetProductsNameResponse.builder().build();
        when(inventoryQueryService.getProductsName(name, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/inventories/products/search")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.names", CoreMatchers.is(response.names())))
                .andReturn();
    }

    @Test
    @DisplayName("제품 이름으로 하위 제품 조회")
    void getSubProductName() throws Exception{
        //given
        String name = "product";
        GetSubProductNameResponse response = GetSubProductNameResponse.builder().build();
        when(inventoryQueryService.getSubProductName(name, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/inventories/subProducts/search")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.subProducts", CoreMatchers.is(response.subProducts())))
                .andReturn();
    }

    @Test
    @DisplayName("재고 검색 - 재고가 존재하는 경우")
    void getProductByKeywordAndInventory() throws Exception{
        //given
        Long inventoryId = 1L;
        String keyword = "product";
        GetProductsResponse response = GetProductsResponse.builder().products(Collections.emptyList()).build();
        when(inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, teamMemberVO)).thenReturn(response);
        when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(true);

        //when & then
        mockMvc.perform(get("/api/v1/inventories/{inventoryId}/search", inventoryId)
                .queryParam("keyword", keyword)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.products", CoreMatchers.is(response.products())))
                .andReturn();
    }

    @Test
    @DisplayName("@ExistInventory - 해당 인벤토리가 없을 때 예외 발생")
    void validExistInventory() throws Exception{
        //given
        Long inventoryId = 1L;
        GetProductsResponse response = GetProductsResponse.builder().products(Collections.emptyList()).build();
        when(inventoryQueryService.getProductsByInventory(inventoryId, teamMemberVO)).thenReturn(response);
        when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(false);

        //when & then
        mockMvc.perform(get("/api/v1/inventories/{inventoryId}", inventoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("@ExistProduct - 해당 제품 없을 때 예외 발생")
    void validExistProduct() throws Exception{
        //given
        Long productId = 1L;
        ModifyProductAmountRequest request = ModifyProductAmountRequest.builder().build();
        when(inventoryQueryService.existByProductId(productId)).thenReturn(false);

        //given & when
        mockMvc.perform(put("/api/v1/inventories/products/{productId}", productId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("제품 추가 - 재고가 존재하는 경우")
    void addProduct() throws Exception{
        //given
        Long inventoryId = 1L;
        AddProductRequest request = AddProductRequest.builder().build();
        AddProductResponse response = AddProductResponse.builder().productId(1L).build();
        when(inventoryQueryService.existByInventoryId(inventoryId)).thenReturn(true);
        when(inventoryCommandService.addProduct(inventoryId, request, teamMemberVO)).thenReturn(response);

        //when & then
        mockMvc.perform(post("/api/v1/inventories/{inventoryId}", inventoryId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.productId", CoreMatchers.is(response.productId().intValue())))
                .andReturn();
    }

    @Test
    @DisplayName("제품 수량 수정 - 제품이 존재 할 때")
    void modifyProductAmount() throws Exception{
        //given
        Long productId = 1L;
        ModifyProductAmountRequest request = ModifyProductAmountRequest.builder().build();
        when(inventoryQueryService.existByProductId(productId)).thenReturn(true);
        doNothing().when(inventoryCommandService).modifyProductAmount(productId, request, teamMemberVO);

        //when & then
        mockMvc.perform(put("/api/v1/inventories/products/{productId}", productId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("제품 삭제 - 제품이 존재 할 때")
    void deleteProduct() throws Exception{
        //given
        Long productId = 1L;
        when(inventoryQueryService.existByProductId(productId)).thenReturn(true);
        doNothing().when(inventoryCommandService).deleteProduct(productId, teamMemberVO);

        //when & then
        mockMvc.perform(delete("/api/v1/inventories/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

}