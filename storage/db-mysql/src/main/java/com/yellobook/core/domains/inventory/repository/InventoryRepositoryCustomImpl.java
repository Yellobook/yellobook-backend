package com.yellobook.core.domains.inventory.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domains.inventory.dto.query.QueryInventory;
import com.yellobook.core.domains.inventory.entity.QInventory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public InventoryRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<QueryInventory> getTotalInventory(Long teamId, Pageable page) {
        QInventory inventory = QInventory.inventory;

        return queryFactory.select(Projections.constructor(QueryInventory.class,
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
