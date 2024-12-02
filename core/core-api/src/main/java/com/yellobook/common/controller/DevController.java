package com.yellobook.common.controller;

import com.yellobook.domains.auth.service.AuthService;
import com.yellobook.domains.auth.service.CookieService;
import com.yellobook.domains.auth.service.JwtService;
import com.yellobook.support.response.ResponseFactory;
import com.yellobook.support.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Profile({"dev", "local"})
@RequestMapping("/api/v1/dev")
@Tag(name = "\uD83D\uDC68\u200D\uD83D\uDCBB 개발용", description = "Dev Token API")
public class DevController {
    private final AuthService authService;
    private final CookieService cookieService;
    private final JwtService jwtService;

    private final JdbcTemplate jdbcTemplate;

    @Operation(summary = "팀 내 권한별 사용자 목록 조회", description = "팀별 ADMIN, ORDERER, VIEWER 한 명씩 조회")
    @GetMapping("/members")
    public ResponseEntity<SuccessResponse<List<TeamMemberRoleResponse>>> getMembersList(
    ) {
        String sql = """
                SELECT team_id, member_id, team_member_role
                FROM (
                    SELECT m.id as member_id, 
                           p.team_member_role, 
                           p.team_id,
                           ROW_NUMBER() OVER (PARTITION BY p.team_id, p.team_member_role ORDER BY m.id) AS row_num
                    FROM participants p
                    JOIN members m ON m.id = p.member_id
                ) AS ranked
                WHERE row_num = 1
                ORDER BY team_id, team_member_role;
                """;

        log.info("[DEV_INFO] 테스트용 임시 사용자 조회");
        var result = jdbcTemplate.query(sql, new TeamMemberRoleRowMapper());
        return ResponseFactory.success(result);
    }

    @Operation(summary = "accessToken, refreshToken 발급", description = "개발시 사용자 id 에 해당하는 토큰을 발급받을 수 있는 API 입니다.")
    @PostMapping("/token")
    public ResponseEntity<SuccessResponse<TokenResponse>> reissueToken(
            @RequestBody TokenRequest request
    ) {
        Long memberId = request.memberId;
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        log.info("[DEV_INFO] 테스트용 임시 사용자의 accessToken, refreshToken 발급");
        var result = new TokenResponse(accessToken, refreshToken);
        return ResponseFactory.success(result);
    }

    public record TokenRequest(
            @Schema(description = "팀에서 사용자의 권한", example = "ADMIN")
            Long memberId
    ) {
    }

    public record TokenResponse(
            String accessToken,
            String refreshToken
    ) {
    }

    public record TeamMemberRoleResponse(
            Long teamId,
            Long memberId,
            String role
    ) {
    }

    private static class TeamMemberRoleRowMapper implements RowMapper<TeamMemberRoleResponse> {
        @Override
        public TeamMemberRoleResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long teamId = rs.getLong("team_id");
            Long memberId = rs.getLong("member_id");
            String teamMemberRole = rs.getString("team_member_role");

            return new TeamMemberRoleResponse(teamId, memberId, teamMemberRole);
        }
    }

}
