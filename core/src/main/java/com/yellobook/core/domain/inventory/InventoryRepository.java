package com.yellobook.core.domain.inventory;

public interface InventoryRepository {
    List<Persistence 의 DTO>

    // 도메인에 dto 를 정의해 놔야하는데 -> 도메인이 오염되는 (조회땜에)
    // 비즈니스적으로 필요한데이터가 있고, 도메인모델이 표현하는 데이터

    // 이래서 CQRS 를 본 이유가 이것도 있음
}
