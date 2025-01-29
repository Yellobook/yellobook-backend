package com.yellobook.core.domain.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Searchable {
    PRIVATE("공개"),
    PUBLIC("비공개"),
    ;

    private final String description;
}
