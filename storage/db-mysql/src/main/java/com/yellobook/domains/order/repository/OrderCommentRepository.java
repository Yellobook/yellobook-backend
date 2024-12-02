package com.yellobook.domains.order.repository;

import com.yellobook.domains.order.entity.OrderComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCommentRepository extends JpaRepository<OrderComment, Long> {
}
