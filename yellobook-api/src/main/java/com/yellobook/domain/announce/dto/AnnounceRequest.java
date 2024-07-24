package com.yellobook.domain.announce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class AnnounceRequest {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostAnnounceRequestDTO {
        private String title;
        private String memo;
        private List<Long> mentioned;
        private Date date;
    }
}
