package com.yellobook.storage.db.core.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCommentJpaRepository extends JpaRepository<OrderCommentEntity, Long> {
}
