package com.yellobook.domain.inventory.mapper;

import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domains.inventory.dto.InventoryDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface InventoryMapper {
    List<GetTotalInventoryResponse.InventoryInfo> toInventoryInfoList(List<InventoryDTO> inventoryDTOList);
}
