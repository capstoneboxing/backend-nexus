package com.nexus.config;

import com.nexus.model.Admin;
import com.nexus.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class DataInitializer {

    @Value("${admin.default.password}")
    private String rawPassword;

    @Bean
    CommandLineRunner init(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminRepository.count() == 0) {
                String[] usernames = {"tywaine", "maurice", "vanessa", "karnardia"};

                for (String username : usernames) {
                    Admin admin = Admin.builder()
                            .username(username)
                            .passwordHash(passwordEncoder.encode(rawPassword))
                            .build();

                    adminRepository.save(admin);
                }

                System.out.println("✅ 4 default admins created");
            }
        };
    }
}