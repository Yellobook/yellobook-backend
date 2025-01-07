package com.yellobook.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMentionJpaRepository extends JpaRepository<OrderMention, Long> {
    boolean existsByMemberIdAndOrderId(Long memberId, Long orderId);

    void deleteAllByOrderId(Long orderId);
}
