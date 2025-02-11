package com.yellobook.api.controller.v1.team;

import com.yellobook.api.controller.v1.team.dto.request.ApproveRejectRequest;
import com.yellobook.api.controller.v1.team.dto.request.CreateTeamRequest;
import com.yellobook.api.controller.v1.team.dto.request.GenerateInvitationCodeRequest;
import com.yellobook.api.controller.v1.team.dto.request.PatchSearchableRequest;
import com.yellobook.api.controller.v1.team.dto.response.CreateTeamResponse;
import com.yellobook.api.controller.v1.team.dto.response.GenerateInvitationCodeResponse;
import com.yellobook.api.controller.v1.team.dto.response.GetParticipantsResponse;
import com.yellobook.api.controller.v1.team.dto.response.GetTeamsResponse;
import com.yellobook.api.controller.v1.team.dto.response.GetTeamsResponse.GetTeamResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.support.api.docs.SwaggerResponse;
import com.yellobook.support.api.docs.SwaggerResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "STORE API", description = "가게")
public interface TeamApiDocs {

    @Operation(summary = "가게 만들기 API", description = "새로운 가게을 생성하는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = CreateTeamResponse.class))
                            }
                    )),
    })
    ApiResponse<CreateTeamResponse> createTeam(@Parameter(description = "생성할 가게의 정보") CreateTeamRequest request,
                                               ApiMember apiMember);


    @Operation(summary = "공개 가게 검색 API", description = "공개 가게을 검색할 수 있는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = GetTeamsResponse.class))
                            }
                    )),
    })
    ApiResponse<GetTeamsResponse> searchPublicTeam(@Parameter(description = "검색하고 싶은 가게의 이름") String name,
                                                   ApiMember apiMember);

    @Operation(summary = "특정 가게 검색 API", description = "특정 가게를 검색할 수 있는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = GetTeamResponse.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "가입하지 않은 비공개 가게를 조회하려고 한 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-03",
                                    "message": "팀에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
            })),
            @SwaggerResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "존재하지 않는 storeId인 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-02",
                                    "message": "가게을 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
            }))
    })
    ApiResponse<GetTeamResponse> getStore(@Parameter(description = "검색하고 싶은 가게의 Id") Long storeId,
                                          @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "가입 요청 API", description = "가게에 가입 요청을 보낼 수 있는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(example = "null", implementation = Void.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "존재하지 않는 storeId인 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-02",
                                    "message": "가게을 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
            })),
            @SwaggerResponse(responseCode = "409", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "이미 가게에 가입했을 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-10",
                                    "message": "이미 가게에 참여한 멤버입니다.",
                                    "data": null
                                }
                            }
                            """),
            }))
    })
    ApiResponse<Void> requestTeamJoin(@Parameter(description = "가입하고 싶은 가게의 Id") Long storeId,
                                      @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "가입 요청 수락&거절 API", description = "가입 요청을 수락 또는 거절하는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(example = "null", implementation = Void.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "가입 요청을 수락&거절하는 사용자가 가게에 가입하지 않은 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-03",
                                    "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
                    @ExampleObject(name = "가입 요청을 수락&거절하는 사용자가 판매자 또는 주문자가 아닌 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-15",
                                    "message": "오직 판매자와 주문자만 가입 요청을 승인 또는 거절할 수 있습니다.",
                                    "data": null
                                }
                            }
                            """),
            })),
            @SwaggerResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "가입 요청이 존재하지 않는 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-16",
                                    "message": "존재하지 않는 가입 요청입니다.",
                                    "data": null
                                }
                            }
                            """),
            })),
            @SwaggerResponse(responseCode = "409", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "이미 가게에 가입했을 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-10",
                                    "message": "이미 가게에 참여한 멤버입니다.",
                                    "data": null
                                }
                            }
                            """),
            }))
    })
    ApiResponse<Void> manageTeamJoinRequest(@Parameter(description = "가게의 Id") Long storeId,
                                            @Parameter(description = "가입 요청한 사용자의 Id") Long memberId,
                                            ApproveRejectRequest dto,
                                            @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "가게 초대 코드 생성 API", description = "가게 초대 코드를 생성하는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = GenerateInvitationCodeResponse.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "초대 코드를 생성하는 사용자가 가게에 가입하지 않은 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-03",
                                    "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
                    @ExampleObject(name = "초대 코드를 생성하는 사용자가 판매자가 아닌 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-06",
                                    "message": "오직 관리자만 초대 코드를 생성할 수 있습니다.",
                                    "data": null
                                }
                            }
                            """),
                    @ExampleObject(name = "주문자를 초대하는 초대 코드를 생성하려는 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-21",
                                    "message": "주문자를 가게에 초대할 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
            }))
    })
    ApiResponse<GenerateInvitationCodeResponse> generateInvitationCode(@Parameter(description = "가게의 Id") Long storeId,
                                                                       @Parameter(description = "어떤 권한의 사용자를 초대할 건지") GenerateInvitationCodeRequest request,
                                                                       @Parameter(hidden = true) ApiMember apiMember);

    // TODO : Return storeId
    @Operation(summary = "코드로 가게 참가 API", description = "코드로 가게에 참가할 수 있는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(example = "null", implementation = Void.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "존재하지 않는 초대 코드인 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-08",
                                    "message": "초대장을 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """)
            })),
            @SwaggerResponse(responseCode = "409", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "가게에 이미 가입했을 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-10",
                                    "message": "이미 가게에 참여한 멤버입니다.",
                                    "data": null
                                }
                            }
                            """)
            }))
    })
    ApiResponse<Void> joinTeamByCode(@Parameter(description = "초대 코드") String code,
                                     @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "뷰어 -> 주문자로 권한 변경 요청 API", description = "뷰어 -> 주문자로의 권한 변경을 요청하는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(example = "null", implementation = Void.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "권한 변경을 요청하는 사용자가 가게에 가입하지 않은 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-03",
                                    "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
                    @ExampleObject(name = "뷰어가 아닌데 뷰어 -> 주문자로 권한 변경을 요청하는 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-17",
                                    "message": "뷰어만 주문자로 권한 변경을 요청할 수 있습니다.",
                                    "data": null
                                }
                            }
                            """)
            }))
    })
    ApiResponse<Void> requestOrdererConversion(@Parameter(description = "가게의 Id") Long storeId,
                                               @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "뷰어 -> 주문자로 권한 변경 요청 수락&거절 API", description = "뷰어 -> 주문자로의 권한 변경 요청을 수락 또는 거절하는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(example = "null", implementation = Void.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "요청을 수락&거절하는 사용자가 가게에 가입하지 않은 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-03",
                                    "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
                    @ExampleObject(name = "판매자가 아닌데 참가자의 권한을 변경하려고 하는 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-13",
                                    "message": "오직 판매자만 변경할 수 있는 정보 입니다.",
                                    "data": null
                                }
                            }
                            """),
                    @ExampleObject(name = "가게에 가입하지 않은 사용자의 권한을 변경하려고 하는 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-03",
                                    "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
            }))
    })
    ApiResponse<Void> changeRoleToOrderer(@Parameter(description = "가게의 Id") Long storeId,
                                          @Parameter(description = "권한 변경을 요청한 사용자의 Id") Long memberId,
                                          @Parameter(description = "승인, 거절 여부") ApproveRejectRequest dto,
                                          @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "가게의 검색 가능 여부 수정 API", description = "가게의 검색 가능 여부를 수정하는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(example = "null", implementation = Void.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "수정을 요청한 사용자가 가게에 가입하지 않은 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,지
                                "error": {
                                    "code": "STORE-03",
                                    "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
                    @ExampleObject(name = "판매자가 아닌데 가게 정보를 수정하려고 하는 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-13",
                                    "message": "오직 판매자만 변경할 수 있는 정보 입니다.",
                                    "data": null
                                }
                            }
                            """)
            }))
    })
    ApiResponse<Void> patchSearchable(@Parameter(description = "가게의 Id") Long storeId,
                                      @Parameter(description = "공개 여부를 무엇으로 변경할 건지") PatchSearchableRequest request,
                                      @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "가게 나가기 API", description = "가게을 탈퇴하는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(example = "null"))
                            }
                    )),
            @SwaggerResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "탈퇴를 요청한 사용자가 가게에 가입한 사용자가 아닌 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-05",
                                    "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """),
                    @ExampleObject(name = "탈퇴를 요청한 사용자가 가게에 존재하는 유일한 판매자인 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-20",
                                    "message": "가게에는 한명 이상의 판매자가 존재해야합니다.",
                                    "data": null
                                }
                            }
                            """),
            })),
            @SwaggerResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "존재하지 않는 TeamId인 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-02",
                                    "message": "가게을 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """)
            })),
    })
    ApiResponse<Void> leaveTeam(@Parameter(description = "가게의 Id") Long storeId,
                                @Parameter(hidden = true) ApiMember apiMember);

    @Operation(summary = "가게에 가입한 사용자 전체 조회 API", description = "가게에 가입한 사용자 모두를 조회하는 API 입니다.")
    @SwaggerResponses({
            @SwaggerResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = GetParticipantsResponse.class))
                            }
                    )),
            @SwaggerResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "조회를 요청한 사용자가 가게의 가입한 사용자가 아닌 경우", value = """
                            {
                                "result" : "ERROR",
                                "data": null,
                                "error": {
                                    "code": "STORE-03",
                                    "message": "가게에서 해당 사용자를 찾을 수 없습니다.",
                                    "data": null
                                }
                            }
                            """)
            }))
    })
    ApiResponse<GetParticipantsResponse> getParticipants(@Parameter(description = "가게의 Id") Long storeId,
                                                         @Parameter(hidden = true) ApiMember apiMember);
}
