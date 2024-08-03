package com.yellobook.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그아웃 요청")
public class LogoutRequest {
    @Schema(description = "로그인한 사용자의 accessToken")
    private String accessToken;
}
