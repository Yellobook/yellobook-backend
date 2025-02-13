package com.yellobook.api.controller.v1.order.dto.request;

import com.yellobook.api.support.pagination.CursorPageRequest;
import com.yellobook.core.domain.order.OrdersCriteria;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public class CustomerOrdersRequest extends CursorPageRequest {
    public CustomerOrdersRequest(Long cursorId, Integer size) {
        super(cursorId, size);
    }

    public OrdersCriteria toOrdersCriteria(long storeId) {
        return new OrdersCriteria(storeId, getCursorId(), getSize());
    }

}
