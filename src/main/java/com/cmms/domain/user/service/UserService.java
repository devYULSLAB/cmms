package com.cmms.domain.user.service;

import com.cmms.domain.user.entity.User;
import com.cmms.domain.user.entity.UserId;
import com.cmms.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getUsersByCompanyId(String companyId) {
        return userRepository.findUsersByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public User getUserById(UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Transactional(readOnly = true)
    public User getUserByCompanyIdAndUserId(String companyId, String userId) {
        return userRepository.findUserByCompanyIdAndUserId(companyId, userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Transactional(readOnly = true)
    public boolean isUserExist(String companyId, String userId) {
        UserId id = new UserId(companyId, userId);
        return userRepository.existsById(id);
    }

    public User saveUser(User user) {
        // This is the original save method, we keep it for the signup process
        // which has its own password handling logic in the controller.
        return userRepository.save(user);
    }

    public User saveUser(User formUser, String rawPassword) {
        UserId userId = new UserId(formUser.getCompanyId(), formUser.getUserId());
        Optional<User> existingUserOpt = userRepository.findById(userId);

        User userToSave;
        if (existingUserOpt.isPresent()) {
            // Update existing user
            userToSave = existingUserOpt.get();
            userToSave.setUserFullName(formUser.getUserFullName());
            userToSave.setIsLocked(formUser.getIsLocked());
            userToSave.setMustChangePw(formUser.getMustChangePw());
            userToSave.setUpdateDate(LocalDateTime.now());
        } else {
            // Create new user
            userToSave = formUser;
            userToSave.setCreateDate(LocalDateTime.now());
            userToSave.setUpdateDate(LocalDateTime.now());
            userToSave.setPasswordUpdatedAt(LocalDateTime.now());
            userToSave.setFailedLoginCount(0);
        }

        // Update password if a new one is provided
        if (StringUtils.hasText(rawPassword)) {
            userToSave.setPasswordHash(passwordEncoder.encode(rawPassword));
            userToSave.setPasswordUpdatedAt(LocalDateTime.now());
        }

        return userRepository.save(userToSave);
    }

    public void deleteUser(UserId userId) {
        userRepository.deleteById(userId);
    }
}
