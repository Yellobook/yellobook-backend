package com.yellobook.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataSourceCommandLineRunner implements CommandLineRunner {
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.driver-class-name}")
    private String datasourceDriver;

    @Override
    public void run(String... args) throws Exception {
        log.info("Datasource URL: {}", datasourceUrl);
        log.info("Datasource Username: {}", datasourceUsername);
        log.info("Datasource Driver: {}", datasourceDriver);
    }
}