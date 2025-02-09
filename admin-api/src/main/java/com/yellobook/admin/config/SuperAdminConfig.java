package com.yellobook.admin.config;

import com.yellobook.admin.security.AdminMemberRole;
import com.yellobook.storage.db.core.AdminMemberEntity;
import com.yellobook.storage.db.core.AdminMemberJpaRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SuperAdminConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public ApplicationRunner initSuperAdmin(SuperAdminProperties properties,
                                            AdminMemberJpaRepository adminMemberJpaRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            adminMemberJpaRepository.findByUsername(properties.username())
                    .ifPresentOrElse(
                            admin -> {
                            },
                            () -> {
                                AdminMemberEntity superAdmin = new AdminMemberEntity(
                                        properties.username(),
                                        passwordEncoder.encode(properties.password()),
                                        UUID.randomUUID()
                                                .toString(),
                                        AdminMemberRole.ROLE_SUPER_ADMIN
                                );
                                adminMemberJpaRepository.save(superAdmin);
                                log.info("Super Admin 계정 생성 - Date: {}", LocalDateTime.now());
                            }
                    );
        };
    }
}
