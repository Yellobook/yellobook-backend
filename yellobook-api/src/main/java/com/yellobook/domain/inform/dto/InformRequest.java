package com.yellobook.domain.inform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class InformRequest {
    @Getter
    @AllArgsConstructor
    public static class CreateInformRequestDTO {
        private String title;
        private String memo;
        private List<Long> mentioned;
        private Date date;
    }
}
