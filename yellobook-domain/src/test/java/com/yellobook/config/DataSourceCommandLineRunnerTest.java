package com.yellobook.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("test")
@DisplayName("DataSourceCommandLineRunner 클래스")
class DataSourceCommandLineRunnerTest {

    // 컨텍스트가 로드되면 run()이 자동으로 호출된다.
    @Nested
    @DisplayName("run 메소드는")
    class Describe_run {

        @Nested
        @DisplayName("애플리케이션이 성공적으로 실행되었다면")
        class Context_run_success {
            @Value("${spring.datasource.url}")
            private String expectedDatasourceUrl;

            @Value("${spring.datasource.username}")
            private String expectedDatasourceUsername;

            @Value("${spring.datasource.driver-class-name}")
            private String expectedDatasourceDriver;

            @Test
            @DisplayName("로그에 데이터소스 관련 정보를 출력해야 한다.")
            void it_log_datasource_properties(CapturedOutput output){

                assertThat(output).contains("Datasource URL: " + expectedDatasourceUrl);
                assertThat(output).contains("Datasource Username: " + expectedDatasourceUsername);
                assertThat(output).contains("Datasource Driver: " + expectedDatasourceDriver);
            }
        }
    }
}
