//package com.yellobook.core.domain.inventory;
//
//import com.yellobook.core.error.CoreErrorType;
//import com.yellobook.core.error.CoreException;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class InventoryReader {
//    private final InventoryRepository inventoryRepository;
//
//    @Autowired
//    public InventoryReader(InventoryRepository inventoryRepository) {
//        this.inventoryRepository = inventoryRepository;
//    }
//
//    public Inventory read(Long inventoryId) {
//        return inventoryRepository.findById(inventoryId)
//                .orElseThrow(
//                        () -> new CoreException(CoreErrorType.INVENTORY_NOT_FOUND)
//                );
//    }
//
//    // 팀의 가장 최근에 저장한 재고를 가지고 온다.
//    public Inventory readLastByTeamIdAndCreatedAt(Long teamId) {
//        return inventoryRepository.findLastByTeamIdAndCreatedAt(teamId)
//                .orElseThrow(
//                        () -> new CoreException(CoreErrorType.TEAM_INVENTORY_EMPTY)
//                );
//    }
//
//    public List<Inventory> readInventoriesByTeamId(Long teamId, Integer page, Integer size) {
//        return inventoryRepository.findInventoriesByTeamId(teamId, page, size);
//    }
//
//
//}
