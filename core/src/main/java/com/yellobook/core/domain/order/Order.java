package com.yellobook.core.domain.order;

import java.time.LocalDate;

// entity (aggregate root)
public record Order(
        Long orderId,
        Orderer orderer,
        OrderStatus orderStatus,
        OrderInfo orderInfo,
        int view,
        String memo,
        LocalDate scheduledDate
) {

}
