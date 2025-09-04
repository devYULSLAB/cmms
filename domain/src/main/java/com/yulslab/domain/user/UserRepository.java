package com.yulslab.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
    // findById is already provided by JpaRepository
}
