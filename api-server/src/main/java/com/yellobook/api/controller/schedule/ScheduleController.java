package com.yellobook.api.controller.schedule;

import com.yellobook.api.controller.schedule.dto.request.CreateScheduleCommentRequest;
import com.yellobook.api.controller.schedule.dto.request.CreateScheduleRequest;
import com.yellobook.api.controller.schedule.dto.response.CreateScheduleCommentResponse;
import com.yellobook.api.controller.schedule.dto.response.CreateScheduleResponse;
import com.yellobook.api.controller.schedule.dto.response.GetScheduleResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.core.domain.schedule.NewScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleCommentService;
import com.yellobook.core.domain.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/teams/{teamId}/schedules")
@Tag(name = "\uD83D\uDCBC 일정", description = "Schedule API")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleCommentService scheduleCommentService;

    public ScheduleController(ScheduleService scheduleService, ScheduleCommentService scheduleCommentService) {
        this.scheduleService = scheduleService;
        this.scheduleCommentService = scheduleCommentService;
    }

    @PostMapping
    @Operation(summary = "일정 생성", description = "새로운 일정을 생성하는 API 입니다.")
    public ApiResponse<CreateScheduleResponse> createSchedule(@PathVariable("teamId") Long teamId,
                                                              @RequestBody
                                                              CreateScheduleRequest createScheduleRequest,
                                                              ApiMember requester) {
        var newSchedule = createScheduleRequest.to(requester.toMember(), teamId);
        var scheduleId = scheduleService.create(newSchedule);
        return ApiResponse.success(new CreateScheduleResponse(scheduleId));
    }

    @DeleteMapping("{scheduleId}")
    @Operation(summary = "일정 삭제", description = "등록된 일정을 삭제하는 API 입니다.")
    public ApiResponse<?> deleteSchedule(ApiMember requester,
                                         @PathVariable Long scheduleId) {
        var deletedScheduleId = scheduleService.delete(scheduleId, requester.toMember());
        scheduleCommentService.deleteByScheduleId(deletedScheduleId);
        return ApiResponse.success();
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary = "일정 조회", description = "등록된 일정를 조회하는 API 입니다.")
    public ApiResponse<GetScheduleResponse> getSchedule(
            @PathVariable("scheduleId") Long scheduleId, ApiMember apiMember
    ) {
        var schedule = scheduleService.read(scheduleId, apiMember.toMember());
        return ApiResponse.success(GetScheduleResponse.from(schedule, scheduleService.getMentions(schedule),
                scheduleCommentService.getComments(schedule)));
    }

    @PostMapping("/{scheduleId}/comment")
    @Operation(summary = "일정 댓글 작성", description = "일정에 댓글을 작성하는 API 입니다.")
    public ApiResponse<CreateScheduleCommentResponse> addComment(
            @PathVariable("scheduleId") Long scheduleId,
            @RequestBody CreateScheduleCommentRequest request,
            ApiMember apiMember
    ) {
        var commentId = scheduleCommentService.add(new NewScheduleComment(apiMember.memberId(),
                scheduleId,
                request.content()));
        return ApiResponse.success(new CreateScheduleCommentResponse(commentId));
    }

    @DeleteMapping("/{scheduleId}/comment/{commentId}")
    @Operation(summary = "일정 댓글 삭제", description = "일정에 댓글을 작성하는 API 입니다.")
    public ApiResponse<?> deleteComment(@PathVariable("commentId") Long commentId, ApiMember apiMember) {
        scheduleCommentService.delete(commentId, apiMember.toMember());
        return ApiResponse.success();
    }
}
