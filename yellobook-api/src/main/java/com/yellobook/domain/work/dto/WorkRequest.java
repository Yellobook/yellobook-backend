package com.yellobook.domain.work.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class WorkRequest {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateWorkRequestDTO {
        private String title;
        private String memo;
        private List<Long> mentioned;
        private Date date;
    }
}
