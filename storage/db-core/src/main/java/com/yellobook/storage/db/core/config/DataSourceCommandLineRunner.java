package com.yellobook.storage.db.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSourceCommandLineRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.driver-class-name}")
    private String datasourceDriver;

    @Override
    public void run(String... args) {
        log.info("Datasource URL: {}", datasourceUrl);
        log.info("Datasource Username: {}", datasourceUsername);
        log.info("Datasource Driver: {}", datasourceDriver);
    }
}