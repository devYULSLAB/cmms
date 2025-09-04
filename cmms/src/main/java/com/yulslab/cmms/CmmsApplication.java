package com.yulslab.cmms;

import com.yulslab.domain.user.User;
import com.yulslab.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = {"com.yulslab.domain", "com.yulslab.cmms"})
@EnableJpaRepositories(basePackages = {"com.yulslab.domain", "com.yulslab.cmms"})
public class CmmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmmsApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User testUser = new User();
            testUser.setUserId("testuser");
            testUser.setPassword(passwordEncoder.encode("password"));
            testUser.setUsername("Test User");
            testUser.setRoles("ROLE_USER");
            testUser.setEnabled(true);
            userRepository.save(testUser);
        };
    }
}
