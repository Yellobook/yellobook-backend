package com.yellobook.api.config;

import com.yellobook.api.support.auth.security.oauth2.SocialType;
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
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"local", "dev"})
@Configuration
public class SwaggerConfig {

    @Value("${backend.base-url}")
    private String backendBaseURL;

    private final List<String> TAG_ORDER = List.of("개발", "로그인", "인증", "사용자", "팀", "일정", "공지", "주문", "재고", "헬스체크");
    private final String SOCIAL_TAG_NAME = "\uD83D\uDE80 소셜 로그인";

    @Bean
    public OpenAPI OpenApiConfig(OpenApiCustomizer openApiCustomizer) {
        Server server = new Server();
        server.setUrl(backendBaseURL);
        server.setDescription("YelloBook Server API");

        OpenAPI openApi = new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                // 기본적으로 모든 엔드포인트에 대한 JWT 인증이 필요한 것으로 설정
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .info(getInfo())
                // 서버 정보 추가
                .servers(List.of(server))
                .tags(List.of(new Tag().name(SOCIAL_TAG_NAME)
                        .description("Oauth2 Endpoint")))
                .path("/oauth2/authorization/kakao", oauth2PathItem(SocialType.KAKAO))
                .path("/oauth2/authorization/naver", oauth2PathItem(SocialType.NAVER));

        openApiCustomizer.customise(openApi);

        return openApi;
    }

    @Bean
    public OpenApiCustomizer customOpenAPI() {
        return openApi -> {
            if (openApi.getTags() != null) {
                List<Tag> openApiTags = new ArrayList<>(openApi.getTags());

                openApiTags.sort((t1, t2) -> {
                    int index1 = getOrderIndex(t1.getName());
                    int index2 = getOrderIndex(t2.getName());
                    return Integer.compare(index1, index2);
                });

                openApi.setTags(openApiTags);
            }
        };
    }

    /**
     * swagger 태그가 지정한 태그 순서에서 몇번째에 위치하는지 반환한다
     *
     * @param tagName swagger 태그 이름
     * @return 태그 순서에서 몇번째에 위치하는지 나타내는 정수
     */
    private int getOrderIndex(String tagName) {
        return IntStream.range(0, TAG_ORDER.size())
                .filter(i -> tagName.contains(TAG_ORDER.get(i)))
                .findFirst()
                // TAG_ORDER 에 존재하지 않으면 맨 마지막 순서에 위치
                .orElse(TAG_ORDER.size());
    }


    private Info getInfo() {
        return new Info()
                .title("YelloBook Server API")
                .description("YelloBook Server API 명세서입니다.")
                .version("v1.0.0");
    }


    private PathItem oauth2PathItem(SocialType socialLoginType) {
        String socialId = socialLoginType.getRegistrationId();
        String socialTitle = socialLoginType.getDescription();
        return new PathItem().get(new Operation()
                .tags(List.of(SOCIAL_TAG_NAME))
                .summary(socialTitle)
                // 인증 비활성화
                .security(List.of())
                .description(String.format("[%s](%s/oauth2/authorization/%s)", socialTitle, backendBaseURL, socialId))
                .responses(new ApiResponses()
                        .addApiResponse("302", new ApiResponse()
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                .type("object")
                                                .example(Map.of(
                                                        "Set-Cookie",
                                                        "accessToken=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c; Max-Age=3600; Path=/; Domain=yellobook.site; HttpOnly=false; Secure=false, refreshToken=dGhpcy1pcy1hLXRlc3QtcmVmcmVzaC10b2tlbg; Max-Age=3600; Path=/; Domain=yellobook.site; HttpOnly=false; Secure=false"
                                                ))))))));
    }
}
