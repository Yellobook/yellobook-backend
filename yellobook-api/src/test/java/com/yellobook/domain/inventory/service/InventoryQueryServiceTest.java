package com.yellobook.domain.inventory.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse.InventoryInfo;
import com.yellobook.domain.inventory.mapper.InventoryMapper;
import com.yellobook.domain.inventory.mapper.ProductMapper;
import com.yellobook.domains.inventory.dto.InventoryDTO;
import com.yellobook.domains.inventory.repository.InventoryRepository;
import com.yellobook.domains.inventory.repository.ProductRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
            List<InventoryDTO> inventoryDTOs = createInventoryDTOs();
            List<InventoryInfo> inventoryInfos = createInventoryInfo();
            when(inventoryRepository.getTotalInventory(admin.getTeamId(), pageable)).thenReturn(inventoryDTOs);
            when(inventoryMapper.toInventoryInfoList(inventoryDTOs)).thenReturn(inventoryInfos);

            //when
            GetTotalInventoryResponse response = inventoryQueryService.getTotalInventory(page, size, admin);

            //then
            verify(inventoryRepository).getTotalInventory(admin.getTeamId(), pageable);
            verify(inventoryMapper).toInventoryInfoList(inventoryDTOs);
            assertThat(response.getPage()).isEqualTo(page);
            assertThat(response.getSize()).isEqualTo(4);
            assertThat(response.getInventories()).isSameAs(inventoryInfos);
        }

        @Test
        @DisplayName("제고가 없으면 빈 리스트를 반환한다.")
        void getEmptyInventory(){
            //given
            Integer page = 1;
            Integer size = 5;
            Pageable pageable = PageRequest.of(page-1, size);
            List<InventoryDTO> inventoryDTOs = Collections.emptyList();
            List<InventoryInfo> inventoryInfos = Collections.emptyList();
            when(inventoryRepository.getTotalInventory(admin.getTeamId(), pageable)).thenReturn(inventoryDTOs);
            when(inventoryMapper.toInventoryInfoList(inventoryDTOs)).thenReturn(inventoryInfos);

            //when
            GetTotalInventoryResponse response = inventoryQueryService.getTotalInventory(page, size, admin);

            //then
            verify(inventoryRepository).getTotalInventory(admin.getTeamId(), pageable);
            verify(inventoryMapper).toInventoryInfoList(inventoryDTOs);
            assertThat(response.getPage()).isEqualTo(page);
            assertThat(response.getSize()).isEqualTo(0);
            assertThat(response.getInventories()).isSameAs(Collections.emptyList());
        }

        private List<InventoryDTO> createInventoryDTOs(){
            List<InventoryDTO> result = new ArrayList<>();
            for(int i =0; i<4; i++){
                result.add(InventoryDTO.builder().build());
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

    }

    @Nested
    @DisplayName("재고 검색")
    class GetProductByKeywordAndInventoryTests{

    }

}
