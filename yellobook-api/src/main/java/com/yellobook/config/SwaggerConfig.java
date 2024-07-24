package com.yellobook.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Map;

@Profile({"local", "dev"})
@Configuration
public class SwaggerConfig {

    @Value("${backend.base-url}")
    private String backendBaseURL;

    @Bean
    public OpenAPI OpenApiConfig() {
        // Servers 에 표시되는 정보 설정
        Server server = new Server();
        server.setUrl(backendBaseURL);
        server.setDescription("YelloBook Server API");

        String SOCIAL_TAG_NAME = "\uD83D\uDE80 소셜 로그인";
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                // 기본적으로 모든 엔드포인트에 대한 JWT 인증이 필요한 것으로 설정
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(getInfo())
                // 서버 정보 추가
                .servers(List.of(server))
                .tags(List.of(new Tag().name(SOCIAL_TAG_NAME).description("Oauth2 Endpoint")))
                .path("/oauth2/authorization/kakao", new PathItem()
                        .get(new Operation()
                                .tags(List.of(SOCIAL_TAG_NAME))
                                .summary("카카오 로그인")
                                // 인증 비활성화
                                .security(List.of())
                                .description(String.format("[카카오 로그인](%s/oauth2/authorization/kakao)", backendBaseURL))
                                .responses(new ApiResponses()
                                        .addApiResponse("302", new ApiResponse()
                                                .content(new Content().addMediaType("application/json",
                                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                                .type("object")
                                                                .example(Map.of(
                                                                        "Set-Cookie", "accessToken=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c; Max-Age=3600; Path=/; Domain=yellobook.site; HttpOnly=false; Secure=false, refreshToken=dGhpcy1pcy1hLXRlc3QtcmVmcmVzaC10b2tlbg; Max-Age=3600; Path=/; Domain=yellobook.site; HttpOnly=false; Secure=false"
                                                                )))))))))

                .path("/oauth2/authorization/naver", new PathItem()
                        .get(new Operation()
                                .tags(List.of(SOCIAL_TAG_NAME))
                                .summary("네이버 로그인")
                                // 인증 비활성화
                                .security(List.of())
                                .description(String.format("[네이버 로그인](%s/oauth2/authorization/naver)", backendBaseURL))
                                .responses(new ApiResponses()
                                        .addApiResponse("302", new ApiResponse()
                                                .content(new Content().addMediaType("application/json",
                                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                                .type("object")
                                                                .example(Map.of(
                                                                        "Set-Cookie", "accessToken=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c; Max-Age=3600; Path=/; Domain=yellobook.site; HttpOnly=false; Secure=false, refreshToken=dGhpcy1pcy1hLXRlc3QtcmVmcmVzaC10b2tlbg; Max-Age=3600; Path=/; Domain=yellobook.site; HttpOnly=false; Secure=false"
                                                                )))))))));
    }

    private Info getInfo() {
        return new Info()
                .title("YelloBook Server API")
                .description("YelloBook Server API 명세서입니다.")
                .version("v1.0.0");
    }
}
