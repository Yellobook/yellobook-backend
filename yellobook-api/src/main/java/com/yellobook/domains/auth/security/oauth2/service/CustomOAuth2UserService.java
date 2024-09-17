package com.yellobook.domains.auth.security.oauth2.service;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2Attributes;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.auth.security.oauth2.enums.SocialType;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        // Oauth2 서비스명
        String registrationId = request.getClientRegistration()
                .getRegistrationId();
        // 인증된 사용자 정보
        Map<String, Object> attributes = oAuth2User.getAttributes();
        // 소셜 로그인 종류
        SocialType socialType = SocialType.from(registrationId);
        // 소셜 로그인 attributes
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(socialType, attributes);

        Member member = memberRepository.findByEmail(oAuth2Attributes.getEmail())
                .orElseGet(() -> {
                    // 사용자가 없다면 생성
                    Member newMember = Member.builder()
                            .nickname(oAuth2Attributes.getNickname())
                            .email(oAuth2Attributes.getEmail())
                            .profileImage(oAuth2Attributes.getProfileImage())
                            .role(MemberRole.USER)
                            .allowance(false)
                            .build();

                    log.info("사용자 가입 이메일: {}", newMember.getEmail());
                    return memberRepository.save(newMember);
                });
        log.info("사용자 로그인 이메일: {}", member.getEmail());
        OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);

        // SecurityContext 에 저장
        return new CustomOAuth2User(oauth2UserDTO);
    }

}
