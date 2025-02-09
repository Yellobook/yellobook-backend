package com.yellobook.api.controller.v1.schedule;

import com.yellobook.api.controller.v1.schedule.dto.request.CreateScheduleCommentRequest;
import com.yellobook.api.controller.v1.schedule.dto.request.CreateScheduleRequest;
import com.yellobook.api.controller.v1.schedule.dto.response.CreateScheduleCommentResponse;
import com.yellobook.api.controller.v1.schedule.dto.response.CreateScheduleResponse;
import com.yellobook.api.controller.v1.schedule.dto.response.GetScheduleResponse;
import com.yellobook.api.support.ApiMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "\uD83D\uDCBC 일정", description = "Schedule EndPoints")
public interface ScheduleApiDocs {

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성하는 Endpoint 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 생성 성공",
                    content = @Content(schema = @Schema(implementation = CreateScheduleResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "USER_NOT_IN_THAT_TEAM",
                                            "message": "해당 팀에 속하지 않은 사용자입니다.",
                                            "data": null
                                        }
                                    }""")
                    ))
    })
    com.yellobook.api.support.response.ApiResponse<CreateScheduleResponse> createSchedule(
            @PathVariable("teamId") Long teamId,
            @RequestBody
            CreateScheduleRequest createScheduleRequest,
            @Parameter(hidden = true) ApiMember requester);

    @Operation(summary = "일정 삭제", description = "등록된 일정을 삭제하는 Endpoint 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "SCHEDULE_AUTHOR_NOT_MATCH",
                                            "message": "공지의 작성자가 아닙니다.",
                                            "data": null
                                        }
                                    }""")
                    )),
            @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "SCHEDULE_NOT_FOUND",
                                            "message": "공지가 존재하지 않습니다.",
                                            "data": {
                                                "scheduleId": 751
                                            }
                                        }
                                    }""")
                    ))
    })
    com.yellobook.api.support.response.ApiResponse<?> deleteSchedule(@Parameter(hidden = true) ApiMember requester,
                                                                     @PathVariable Long scheduleId);

    @Operation(summary = "일정 조회", description = "등록된 일정를 조회하는 Endpoint 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetScheduleResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "SCHEDULE_ACCESS_NOT_ALLOWED",
                                            "message": "공지에 접근할 권한이 없습니다.",
                                            "data": null
                                        }
                                    }""")
                    )),
            @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "SCHEDULE_NOT_FOUND",
                                            "message": "공지가 존재하지 않습니다.",
                                            "data": {
                                                "scheduleId": 751
                                            }
                                        }
                                    }""")
                    ))
    })
    com.yellobook.api.support.response.ApiResponse<GetScheduleResponse> getSchedule(
            @PathVariable("scheduleId") Long scheduleId, @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "일정 댓글 작성", description = "일정에 댓글을 작성하는 Endpoint 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공",
                    content = @Content(schema = @Schema(implementation = CreateScheduleCommentResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "SCHEDULE_ACCESS_NOT_ALLOWED",
                                            "message": "공지에 접근할 권한이 없습니다.",
                                            "data": null
                                        }
                                    }""")
                    )),
            @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "SCHEDULE_NOT_FOUND",
                                            "message": "공지가 존재하지 않습니다.",
                                            "data": {
                                                "scheduleId": 751
                                            }
                                        }
                                    }""")
                    ))
    })
    com.yellobook.api.support.response.ApiResponse<CreateScheduleCommentResponse> addComment(
            @PathVariable("scheduleId") Long scheduleId,
            @RequestBody CreateScheduleCommentRequest request,
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "일정 댓글 삭제", description = "일정에 댓글을 삭제하는 Endpoint 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "SCHEDULE_COMMENT_AUTHOR_NOT_MATCH",
                                            "message": "공지 댓글의 작성자가 아닙니다.",
                                            "data": null
                                        }
                                    }""")
                    )),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "SCHEDULE_COMMENT_AUTHOR_NOT_MATCH",
                                            "message": "공지 댓글의 작성자가 아닙니다.",
                                            "data": {
                                                "commentId": 123
                                            }
                                        }
                                    }""")
                    ))
    })
    com.yellobook.api.support.response.ApiResponse<?> deleteComment(@PathVariable("commentId") Long commentId,
                                                                    @Parameter(hidden = true) ApiMember apiMember);
}
