package com.yellobook.api.support;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yellobook.core.domain.member.ProfileInfo;
import com.yellobook.core.domain.member.SocialInfo;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public abstract class ControllerTest {

    @MockBean
    ApiMemberArgumentResolver apiMemberArgumentResolver;

    @BeforeEach
    void setup() throws Exception {
        given(apiMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(new ApiMember(
                        1L,
                        new SocialInfo("oauth123", "naver", "test@example.com"),
                        new ProfileInfo("nickname", "bio", "profile.png", LocalDateTime.now())
                ));
    }
}
