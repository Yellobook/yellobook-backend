package com.yellobook.config;

import static org.assertj.core.api.Assertions.assertThat;
import static support.ReflectionUtil.setField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
@DisplayName("DataSourceCommandLineRunner 클래스")
class DataSourceCommandLineRunnerTest {

    @InjectMocks
    private DataSourceCommandLineRunner dataSourceCommandLineRunner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
        // 리플렉션을 사용해 private 필드에 값을 설정
        setField(dataSourceCommandLineRunner, "datasourceUrl", "jdbc:h2:mem:testdb");
        setField(dataSourceCommandLineRunner, "datasourceUsername", "testuser");
        setField(dataSourceCommandLineRunner, "datasourceDriver", "org.h2.Driver");
    }

    @Nested
    @DisplayName("run 메소드는")
    class Describe_run {

        @Nested
        @DisplayName("애플리케이션이 성공적으로 실행되었다면")
        class Context_run_success {

            @Test
            @DisplayName("로그에 데이터소스 관련 정보를 출력해야 한다.")
            void it_log_datasource_properties(CapturedOutput output) throws Exception {
                dataSourceCommandLineRunner.run();

                // 로그 메시지 검증
                assertThat(output).contains("Datasource URL: jdbc:h2:mem:testdb");
                assertThat(output).contains("Datasource Username: testuser");
                assertThat(output).contains("Datasource Driver: org.h2.Driver");
            }
        }
    }
}