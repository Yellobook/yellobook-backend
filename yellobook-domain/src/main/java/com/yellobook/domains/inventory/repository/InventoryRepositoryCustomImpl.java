package com.yellobook.domains.inventory.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.inventory.dto.InventoryDTO;
import com.yellobook.domains.inventory.entity.QInventory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QInventory inventory = QInventory.inventory;

    public InventoryRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<InventoryDTO> getTotalInventory(Long teamId, Pageable page) {
        return queryFactory.select(Projections.constructor(InventoryDTO.class,
                        inventory.id.as("inventoryId"),
                        inventory.title,
                        inventory.createdAt,
                        inventory.updatedAt,
                        inventory.view))
                .from(inventory)
                .where(inventory.team.id.eq(teamId))
                .orderBy(inventory.createdAt.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();
    }
}
