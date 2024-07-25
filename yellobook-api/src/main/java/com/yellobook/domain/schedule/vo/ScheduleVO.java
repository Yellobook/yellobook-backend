package com.yellobook.domain.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


// 데이터계층에서 받은 DTO 를 VO 로 변환하고 컨트롤러 DTO 에 담아 전송한다.
// 만약 데이터계층에 받은 DTO 를 그대로 전달하면 Swagger 의존성이 domain 모듈에도 필요함 - 생각 필요
@Getter
@RequiredArgsConstructor
@Schema(description = "일정 VO")
public class ScheduleVO {
    @Schema(description = "일정 ID", example = "")
    private final Long id;

    @Schema(description = "일정 제목")
    private final String title;

    @Schema(description = "일정 VO")
    private final String postType;
}
