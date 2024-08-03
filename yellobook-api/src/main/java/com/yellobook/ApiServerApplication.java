package com.yellobook;

import com.yellobook.domain.auth.service.JwtService;
import com.yellobook.domain.auth.service.RedisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class ApiServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args);
	}
}
