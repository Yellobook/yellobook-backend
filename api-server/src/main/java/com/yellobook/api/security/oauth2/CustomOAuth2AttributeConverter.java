package com.yellobook.api.security.oauth2;

import com.yellobook.api.security.error.AuthErrorType;
import com.yellobook.api.security.error.AuthException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2AttributeConverter
        implements Converter<OAuth2UserRequest, Converter<Map<String, Object>, Map<String, Object>>> {

    @Override
    public Converter<Map<String, Object>, Map<String, Object>> convert(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();

        return attributes -> {
            Map<String, Object> result = new HashMap<>();
            if ("kakao".equals(registrationId)) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

                result.put("oauthId", attributes.get("id"));
                result.put("provider", registrationId);
                result.put("email", kakaoAccount.get("email"));
                result.put("nickname", profile.get("nickname"));
                result.put("profileImage", profile.get("profile_image_url"));

            } else if ("naver".equals(registrationId)) {
                Map<String, String> response = (Map<String, String>) attributes.get("response");

                result.put("oauthId", response.get("id"));
                result.put("provider", registrationId);
                result.put("email", response.get("email"));
                result.put("nickname", response.get("nickname"));
                result.put("profileImage", response.get("profile_image"));

            } else {
                throw new AuthException(AuthErrorType.OAUTH2_PROVIDER_INVALID);
            }
            return result;
        };
    }
}
