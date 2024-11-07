package com.yellobook.domains.inventory.mapper;

import static com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse.InventoryInfo;

import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.inventory.dto.response.AddInventoryResponse;
import com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.team.entity.Team;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    List<InventoryInfo> toInventoryInfoList(List<QueryInventory> queryInventoryList);

    @Mapping(target = "size", expression = "java(content.size())")
    @Mapping(source = "content", target = "inventories")
    GetTotalInventoryResponse toGetTotalInventoryResponse(Integer page, List<QueryInventory> content);

    Inventory toInventory(Team team, String title);

    AddInventoryResponse toAddInventoryResponse(Long inventoryId, List<Long> productIds);
}
