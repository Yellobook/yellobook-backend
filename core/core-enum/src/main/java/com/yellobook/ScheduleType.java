package com.yellobook;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleType {
    INFORM("INFORM"),
    ORDER("ORDER");
    private final String value;
}