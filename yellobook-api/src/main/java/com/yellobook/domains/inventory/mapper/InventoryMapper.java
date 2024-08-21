package com.yellobook.domains.inventory.mapper;

import com.yellobook.domains.inventory.dto.query.QueryInventory;
import com.yellobook.domains.inventory.dto.response.AddInventoryResponse;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.team.entity.Team;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.yellobook.domains.inventory.dto.response.GetTotalInventoryResponse.*;

@Component
@Mapper(componentModel = "spring")
public interface InventoryMapper {
    List<InventoryInfo> toInventoryInfoList(List<QueryInventory> queryInventoryList);
    Inventory toInventory(Team team, String title);
    AddInventoryResponse toAddInventoryResponse(Long inventoryId, List<Long> productIds);
}
