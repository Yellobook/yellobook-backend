package com.yellobook.support.api.docs;

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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile({"local", "dev"})
@Configuration
public class SwaggerConfig {
    private final DocsServerProperties properties;

    public SwaggerConfig(DocsServerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI OpenApiConfig() {
        Server server = new Server();
        server.setUrl(properties.apiUrl());
        server.setDescription("YelloBook Server API");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        OpenAPI openApi = new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement)
                .info(getInfo())
                .servers(List.of(server));

        return openApi;
    }

    @Bean
    public OpenApiCustomizer customOpenAPI() {
        List<String> tagOrders = List.of("SOCIAL LOGIN", "AUTH", "MEMBER", "TERMS", "ORDER", "STOCK", "HEALTH");
        return openApi -> {
            List<Tag> tags = openApi.getTags();
            tags.add(new Tag().name("SOCIAL LOGIN API")
                    .description("Social Login Endpoint"));
            setOauth2(openApi);
            Map<String, Schema> schemas = openApi.getComponents()
                    .getSchemas();
            openApi.getComponents()
                    .setSchemas(new TreeMap<>(schemas));

            openApi.setTags(
                    openApi.getTags()
                            .stream()
                            .sorted(Comparator.comparingInt(tag -> IntStream.range(0, tagOrders.size())
                                    .filter(i -> tag.getName()
                                            .contains(tagOrders.get(i)))
                                    .findFirst()
                                    .orElse(tagOrders.size())))
                            .toList());
        };
    }

    private Info getInfo() {
        return new Info()
                .title("YelloBook Server API")
                .description(getApiDescription())
                .version("v1.0.0");
    }

    private void setOauth2(OpenAPI openAPI) {
        openAPI.getPaths()
                .addPathItem("/oauth2/authorization/kakao", oauth2PathItem(DocsSocialType.KAKAO));
        openAPI.getPaths()
                .addPathItem("/oauth2/authorization/naver", oauth2PathItem(DocsSocialType.NAVER));
    }

    private PathItem oauth2PathItem(DocsSocialType socialLoginType) {
        String provider = socialLoginType.getProvider();
        String displayName = socialLoginType.getDisplayName();
        return new PathItem().get(new Operation()
                .tags(List.of("SOCIAL LOGIN API"))
                .summary(displayName)
                .security(List.of())
                .description(
                        String.format("[%s](%s/oauth2/authorization/%s)", displayName, properties.apiUrl(), provider))
                .responses(new ApiResponses()
                        .addApiResponse("302", new ApiResponse()
                                .description(String.format("%s 후 리다이렉트", displayName))
                                .content(new Content().addMediaType("application/json",
                                        new MediaType()
                                                .schema(new Schema<Map<String, String>>()
                                                        .type("object")
                                                        .example(Map.of(
                                                                "Set-Cookie",
                                                                "accessToken=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c; Max-Age=3600; Path=/; Domain=yellobook.site; HttpOnly=false; Secure=false, refreshToken=dGhpcy1pcy1hLXRlc3QtcmVmcmVzaC10b2tlbg; Max-Age=3600; Path=/; Domain=yellobook.site; HttpOnly=false; Secure=false"
                                                        ))))))));
    }

    private String getApiDescription() {
        return """
                YelloBook Server API 명세서입니다.
                            
                ### 응답 형식

                프로젝트는 다음과 같은 응답 형식을 제공합니다.
                                
                #### 성공(200,OK)
                <table>
                  <thead>
                    <tr>
                      <th>응답 데이터가 있는 경우</th>
                      <th>&nbsp;&nbsp;</th>
                      <th>응답 데이터가 없는 경우</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>
                        <pre>
                        <code>
                {
                  "result": "SUCCESS", // 성공이면 'SUCCESS'
                  "data": {  // 응답 데이터
                     "memberId": 1,
                     "bio": "옐로북 개발자"
                     ...
                  }
                }
                        </code>
                        </pre>
                      </td>
                      <td>&nbsp;&nbsp;</td>
                      <td>
                        <pre>
                        <code>
                {
                  "result": "SUCCESS", // 성공이면 'SUCCESS'
                  "data": null // 데이터가 없으면 null
                }
                        </code>
                        </pre>
                      </td>
                    </tr>
                  </tbody>
                </table>
                                
                #### 실패        
                <table>
                  <thead>
                    <tr>
                      <th>응답 데이터가 있는 경우</th>
                      <th>&nbsp;&nbsp;</th>
                      <th>응답 데이터가 없는 경우</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>
                        <pre>
                        <code>
                {
                  "result": "ERROR", // 실패면 "ERROR"
                  "error": {
                      "code": "VAL01", // 에러코드
                      "message": "요청한 데이터가 유효하지 않습니다.", // 에러 메시지
                      "data": [ // 에러 응답에 추가 데이터 반환 필요시 기재
                          {
                              "field": "email",
                              "message": "이메일 형식이 올바르지 않습니다."
                          },
                          ...
                      ]
                  }
                }
                        </code>
                        </pre>
                      </td>
                      <td>&nbsp;&nbsp;</td>
                      <td>
                        <pre>
                        <code>
                 {
                  "result": "ERROR", // 실패면 "ERROR"
                  "error": {
                      "code": "VAL01", // 에러코드
                      "message": "요청한 데이터가 유효하지 않습니다.", // 에러 메시지
                      "data": null // 데이터가 없으면 null
                  }
                }
                        </code>
                        </pre>
                      </td>
                    </tr>
                  </tbody>
                </table>
                """;
    }
}
