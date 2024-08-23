package com.yellobook.domains.inventory.mapper;

import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse.*;

@Component
@Mapper(componentModel = "spring")
public interface InventoryMapper {
    List<InventoryInfo> toInventoryInfoList(List<QueryInventory> queryInventoryList);
    GetTotalInventoryResponse toGetTotalInventoryResponse(Integer page, Integer size, List<InventoryInfo> inventories);

}
