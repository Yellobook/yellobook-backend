package com.yellobook.inventory;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.inventory.InventoryRepository;
import com.yellobook.team.TeamEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryDao implements InventoryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final InventoryJpaRepository inventoryJpaRepository;
    private final TeamJpaRepository teamJpaRepository;

    public InventoryDao(JPAQueryFactory jpaQueryFactory, InventoryJpaRepository inventoryJpaRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.inventoryJpaRepository = inventoryJpaRepository;
    }

    // 조회관련이라서 dto 로 넘겨야 하고 domain 엔티티를 사용할 수 없을 때
    @Override
    public List<com.yellobook.domains.inventory.dto.query.QueryInventory> getTotalInventory(Long teamId,
                                                                                            Pageable page) {
        QInventory inventory = QInventory.inventory;

        return queryFactory.select(
                        Projections.constructor(com.yellobook.domains.inventory.dto.query.QueryInventory.class,
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

    @Override
    public Long save(Long teamId, String title) {
        TeamEntity teamEntity = teamJpaRepository.getReferenceById(teamId);
        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .team(teamEntity)
                .title(title)
                .build();
        return inventoryJpaRepository.save(inventoryEntity)
                .getId();
    }
}



