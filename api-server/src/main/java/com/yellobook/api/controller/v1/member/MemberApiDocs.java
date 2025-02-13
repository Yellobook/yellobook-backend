package com.yellobook.api.controller.v1.member;

import com.yellobook.api.controller.member.dto.request.UpdateBioRequest;
import com.yellobook.api.controller.member.dto.request.UpdateNicknameRequest;
import com.yellobook.api.controller.member.dto.response.JoinedTeamsResponse;
import com.yellobook.api.controller.member.dto.response.ProfileResponse;
import com.yellobook.api.support.ApiMember;
import com.yellobook.api.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "MEMBER API", description = "회원 관련 API")
interface MemberApiDocs {

    @Operation(summary = "마이프로필 조회", description = "사용자의 프로필 정보를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = ProfileResponse.class))
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER01"),
    })
    ApiResponse<ProfileResponse> getMemberProfile(
            @Parameter(hidden = true) ApiMember apiMember
    );

    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다. 30일에 한 번 닉네임을 변경할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content()
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER01"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "MEMBER03")
    })
    ApiResponse<?> updateNickname(
            @Parameter(hidden = true) ApiMember apiMember,
            UpdateNicknameRequest request
    );


    @Operation(summary = "한줄 소개 변경", description = "사용자의 한줄소개를 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = ProfileResponse.class))
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER01"),
    })
    ApiResponse<?> updateBio(
            @Parameter(hidden = true) ApiMember apiMember,
            UpdateBioRequest request
    );

    @Operation(summary = "소속된 가게 목록 조회", description = "사용자가 소속된 가게 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = JoinedTeamsResponse.class))
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER01"),
    })
    ApiResponse<JoinedTeamsResponse> getMemberJoinedTeams(
            @Parameter(hidden = true)
            ApiMember apiMember
    );
}