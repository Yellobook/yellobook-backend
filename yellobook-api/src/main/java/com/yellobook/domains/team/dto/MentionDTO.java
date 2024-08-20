package com.yellobook.domains.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MentionDTO {
    private List<Long> ids;
}
