package com.yellobook.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI YelloBookAPI() {

        Info info = new Info()
                .title("YelloBook API")
                .description("YelloBook API 명세서")
                .version("1.0.0");

        SecurityScheme kakaoScheme = createOAuthScheme("Kakao", "/oauth2/authorization/kakao", kakaoScope());
        SecurityScheme naverScheme = createOAuthScheme("Naver", "/oauth2/authorization/naver", naverScope());


        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("oauth2"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Kakao", kakaoScheme)
                        .addSecuritySchemes("Naver", naverScheme)
                );
    }

    private Scopes kakaoScope(){
        return new Scopes(){{
            addString("profile_nickname", "[필수] 닉네임");
            addString("profile_image", "[필수] 프로필 이미지");
            addString("account_email", "[필수] 이메일");
        }};
    }

    private Scopes naverScope(){
        return new Scopes(){{
            addString("nickname", "[필수] 닉네임");
            addString("email", "[필수] 이메일");
            addString("profile_image", "[필수] 프로필 이미지");
        }};
    }

    private SecurityScheme createOAuthScheme(String platform, String authorizationUrl, Scopes scopes){
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .in(SecurityScheme.In.HEADER)
                .name(platform)
                .flows(new OAuthFlows()
                        .authorizationCode(
                                new OAuthFlow()
                                        .authorizationUrl(authorizationUrl)
                                        .scopes(scopes)
                        )
                );
    }
}
