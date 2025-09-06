package com.cmms.domain.user.repository;

import com.cmms.domain.user.entity.User;
import com.cmms.domain.user.entity.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {

    List<User> findByCompanyId(String companyId);

    Optional<User> findByCompanyIdAndUserId(String companyId, String userId);
}
