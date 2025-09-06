package com.cmms.domain.user.service;

import com.cmms.domain.user.entity.User;
import com.cmms.domain.user.entity.UserId;
import com.cmms.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> findAllByCompanyId(String companyId) {
        return userRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public User findUserById(UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User saveUser(User user) {
        // Password encoding should be handled here before saving
        return userRepository.save(user);
    }

    public void deleteUser(UserId userId) {
        userRepository.deleteById(userId);
    }
}
