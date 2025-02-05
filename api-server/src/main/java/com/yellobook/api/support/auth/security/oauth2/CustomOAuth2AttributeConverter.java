package com.yellobook.api.support.auth.security.oauth2;

import com.yellobook.api.support.auth.error.AuthErrorType;
import com.yellobook.api.support.auth.error.AuthException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2AttributeConverter {
    public Converter<OAuth2UserRequest, Converter<Map<String, Object>, Map<String, Object>>> getConverter() {
        return (userRequest) -> (attributes) -> {
            String registrationId = userRequest.getClientRegistration()
                    .getRegistrationId();

            if ("kakao".equals(registrationId)) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                Map<String, Object> result = new HashMap<>();
                result.put("id", attributes.get("id"));
                result.put("name", registrationId);
                result.put("email", kakaoAccount.get("email"));
                result.put("nickname", profile.get("nickname"));
                result.put("profileImage", profile.get("profile_image_url"));
                return result;

            } else if ("naver".equals(registrationId)) {
                Map<String, String> response = (Map<String, String>) attributes.get("response");
                Map<String, Object> result = new HashMap<>();
                result.put("oauthId", response.get("id"));
                result.put("provider", registrationId);
                result.put("email", response.get("email"));
                result.put("name", response.get("nickname"));
                result.put("profileImage", response.get("profile_image"));
                return result;
            }
            throw new AuthException(AuthErrorType.OAUTH2_PROVIDER_INVALID);
        };
    }
}
