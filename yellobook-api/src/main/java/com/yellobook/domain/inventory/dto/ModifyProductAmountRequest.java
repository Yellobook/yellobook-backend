package com.yellobook.domain.inventory.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyProductAmountRequest {
    private Integer amount;
}
