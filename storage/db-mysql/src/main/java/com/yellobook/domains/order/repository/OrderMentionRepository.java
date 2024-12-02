package com.yellobook.domains.order.repository;

import com.yellobook.domains.order.entity.OrderMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMentionRepository extends JpaRepository<OrderMention, Long> {
    boolean existsByMemberIdAndOrderId(Long memberId, Long orderId);

    void deleteAllByOrderId(Long orderId);
}
