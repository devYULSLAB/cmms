package com.yulslab.cmms;

import com.yulslab.domain.user.User;
import com.yulslab.domain.user.UserId;
import com.yulslab.domain.user.UserRepository;
import java.time.LocalDateTime;
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
            UserId userId = new UserId();
            userId.setCompanyId("C0001");
            userId.setUserId("testuser");

            if (userRepository.findById(userId).isEmpty()) {
                User testUser = new User();
                testUser.setId(userId);
                testUser.setUserName("Test User");
                testUser.setPasswordHash(passwordEncoder.encode("password"));
                testUser.setPasswordUpdatedAt(LocalDateTime.now());
                testUser.setFailedLoginCount(0);
                testUser.setIsLocked('N');
                testUser.setMustChangePw('N');
                userRepository.save(testUser);
            }
        };
    }
}
