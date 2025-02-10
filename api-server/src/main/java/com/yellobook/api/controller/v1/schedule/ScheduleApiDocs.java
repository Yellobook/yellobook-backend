package com.yellobook.api.controller.v1.schedule;

import com.yellobook.api.controller.v1.schedule.dto.request.CreateScheduleCommentRequest;
import com.yellobook.api.controller.v1.schedule.dto.request.CreateScheduleRequest;
import com.yellobook.api.controller.v1.schedule.dto.response.CreateScheduleCommentResponse;
import com.yellobook.api.controller.v1.schedule.dto.response.CreateScheduleResponse;
import com.yellobook.api.controller.v1.schedule.dto.response.GetMemberMentionResponse;
import com.yellobook.api.controller.v1.schedule.dto.response.GetScheduleResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "\uD83D\uDCBC 일정", description = "Schedule EndPoints")
public interface ScheduleApiDocs {

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성하는 Endpoint 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정 생성 성공",
                    content = @Content(schema = @Schema(implementation = CreateScheduleResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음",
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
    ApiResponse<CreateScheduleResponse> createSchedule(
            @PathVariable("teamId") Long teamId,
            @RequestBody
            CreateScheduleRequest createScheduleRequest,
            @Parameter(hidden = true) ApiMember requester);

    @Operation(summary = "일정 삭제", description = "등록된 일정을 삭제하는 Endpoint 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음",
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음",
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
    ApiResponse<?> deleteSchedule(@Parameter(hidden = true) ApiMember requester,
                                  @PathVariable Long scheduleId);

    @Operation(summary = "일정 조회", description = "등록된 일정를 조회하는 Endpoint 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetScheduleResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음",
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음",
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
    ApiResponse<GetScheduleResponse> getSchedule(
            @PathVariable("scheduleId") Long scheduleId, @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "일정 댓글 작성", description = "일정에 댓글을 작성하는 Endpoint 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 작성 성공",
                    content = @Content(schema = @Schema(implementation = CreateScheduleCommentResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음",
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음",
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
    ApiResponse<CreateScheduleCommentResponse> addComment(
            @PathVariable("scheduleId") Long scheduleId,
            @RequestBody CreateScheduleCommentRequest request,
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "일정 댓글 삭제", description = "일정에 댓글을 삭제하는 Endpoint 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음",
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
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
    ApiResponse<?> deleteComment(@PathVariable("commentId") Long commentId,
                                 @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "일정 멘션 멤버 검색", description = "일정 멘션을 위한 검색을 지원하는 Endpoint입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(schema = @Schema(implementation = GetMemberMentionResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "result": "ERROR",
                                        "data": null,
                                        "error":{
                                            "code": "USER_NOT_IN_THE_STORE",
                                            "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                            "data": null
                                        }
                                    }""")
                    ))
    })
    ApiResponse<GetMemberMentionResponse> searchMemberMention(ApiMember apiMember,
                                                              @PathVariable("teamId") Long teamId,
                                                              @RequestParam(required = false) String keyword);
}
