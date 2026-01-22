package com.linkchat.config;

import com.linkchat.application.service.UserApplicationService;
import com.linkchat.domain.user.repository.UserRepository;
import com.linkchat.domain.user.service.UserDomainService;
import com.linkchat.infrastructure.persistence.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DomainConfig {
    @Bean
    public UserDomainService userDomainService(UserRepository userRepository) {
        return new UserDomainService(userRepository);
    }

    @Bean
    public UserApplicationService userApplicationService(UserRepository userRepository, UserDomainService userDomainService, PasswordEncoder passwordEncoder) {
        return new UserApplicationService(userRepository, userDomainService, passwordEncoder);
    }
}