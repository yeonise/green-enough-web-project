package com.green.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}

// JPA 와 AuditorAware 를 사용하면 간단한 매핑을 통해 특정 필드에 현재 로그인한 사람의 정보를 등록자로 자동으로 입력할 수 있다.