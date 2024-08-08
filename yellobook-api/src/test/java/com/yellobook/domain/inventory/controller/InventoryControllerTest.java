package com.yellobook.domain.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.resolver.TeamMemberArgumentResolver;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.inventory.dto.request.ModifyProductAmountRequest;
import com.yellobook.domain.inventory.dto.response.GetProductsResponse;
import com.yellobook.domain.inventory.dto.response.GetTotalInventoryResponse;
import com.yellobook.domain.inventory.service.InventoryCommandService;
import com.yellobook.domain.inventory.service.InventoryQueryService;
import com.yellobook.response.ResponseFactory;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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









}