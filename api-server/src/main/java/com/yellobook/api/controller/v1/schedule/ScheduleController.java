package com.yellobook.api.controller.v1.schedule;

import com.yellobook.api.controller.v1.schedule.dto.request.CreateScheduleCommentRequest;
import com.yellobook.api.controller.v1.schedule.dto.request.CreateScheduleRequest;
import com.yellobook.api.controller.v1.schedule.dto.response.CreateScheduleCommentResponse;
import com.yellobook.api.controller.v1.schedule.dto.response.CreateScheduleResponse;
import com.yellobook.api.controller.v1.schedule.dto.response.GetMemberMentionResponse;
import com.yellobook.api.controller.v1.schedule.dto.response.GetScheduleResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.core.domain.schedule.NewScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleCommentService;
import com.yellobook.core.domain.schedule.ScheduleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/teams/{teamId}/schedules")
public class ScheduleController implements ScheduleApiDocs {

    private final ScheduleService scheduleService;
    private final ScheduleCommentService scheduleCommentService;

    public ScheduleController(ScheduleService scheduleService, ScheduleCommentService scheduleCommentService) {
        this.scheduleService = scheduleService;
        this.scheduleCommentService = scheduleCommentService;
    }

    @PostMapping
    public ApiResponse<CreateScheduleResponse> createSchedule(@PathVariable("teamId") Long teamId,
                                                              @RequestBody
                                                              CreateScheduleRequest createScheduleRequest,
                                                              ApiMember requester) {
        var newSchedule = createScheduleRequest.to(requester.toMember(), teamId);
        var scheduleId = scheduleService.create(newSchedule);
        return ApiResponse.success(new CreateScheduleResponse(scheduleId));
    }

    @DeleteMapping("{scheduleId}")
    public ApiResponse<?> deleteSchedule(ApiMember requester,
                                         @PathVariable Long scheduleId) {
        var deletedScheduleId = scheduleService.delete(scheduleId, requester.toMember());
        scheduleCommentService.deleteByScheduleId(deletedScheduleId);
        return ApiResponse.success();
    }

    @GetMapping("/{scheduleId}")
    public ApiResponse<GetScheduleResponse> getSchedule(
            @PathVariable("scheduleId") Long scheduleId, ApiMember apiMember
    ) {
        var schedule = scheduleService.read(scheduleId, apiMember.toMember());
        return ApiResponse.success(GetScheduleResponse.from(schedule, scheduleService.getMentions(schedule),
                scheduleCommentService.getComments(schedule)));
    }

    @PostMapping("/{scheduleId}/comment")
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
    public ApiResponse<?> deleteComment(@PathVariable("commentId") Long commentId, ApiMember apiMember) {
        scheduleCommentService.delete(commentId, apiMember.toMember());
        return ApiResponse.success();
    }

    @GetMapping("/search")
    public ApiResponse<GetMemberMentionResponse> searchMemberMention(ApiMember apiMember,
                                                                     @PathVariable("teamId") Long teamId,
                                                                     @RequestParam(required = false) String keyword) {
        var members = scheduleService.findMembers(keyword, teamId, apiMember.toMember());
        return ApiResponse.success(GetMemberMentionResponse.from(members));
    }
}
