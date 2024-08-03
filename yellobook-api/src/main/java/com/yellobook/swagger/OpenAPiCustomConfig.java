package com.yellobook.swagger;

import io.swagger.v3.oas.models.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Profile({"local", "dev"})
@Configuration
public class OpenAPiCustomConfig {

    private final List<String> TAG_ORDER =  List.of("로그인", "인증", "사용자", "팀", "일정", "업무 및 공지","주문", "재고", "헬스체크");

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
}
