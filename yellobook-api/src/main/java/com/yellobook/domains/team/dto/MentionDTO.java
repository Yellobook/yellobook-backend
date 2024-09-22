package com.yellobook.domains.team.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MentionDTO {
    private List<Long> ids;
}
