package com.yellobook.storage.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.yellobook.core", "com.yellobook.storage.db.core"})
public class CoreDbTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreDbTestApplication.class);
    }
}
