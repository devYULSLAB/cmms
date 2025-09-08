package com.cmms.auth.service;

import com.cmms.domain.user.entity.User;
import com.cmms.domain.user.repository.UserRepository;
import com.cmms.domain.role.entity.Role;
import com.cmms.domain.role.repository.RoleRepository;
import com.cmms.domain.company.entity.Company;
import com.cmms.domain.company.repository.CompanyRepository;
import com.cmms.domain.dept.entity.Dept;
import com.cmms.domain.dept.repository.DeptRepository;
import com.cmms.auth.dto.CustomUserDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * cmms - CustomUserDetailsService
 * Spring Security의 UserDetailsService 구현체
 * 
 * @author cmms
 * @since 2024-03-19
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final DeptRepository deptRepository;
    // default company ID
    // private static final String DEFAULT_COMPANY_ID = "C0001";
    @Value("${cmms.default-company-id}")
    private String defaultCompanyId;

    public CustomUserDetailsService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            DeptRepository deptRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.deptRepository = deptRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== CustomUserDetailsService.loadUserByUsername ===");
        System.out.println("Attempting to load user with username: " + username);

        // 사용자 정보 조회 (테스트 목적이므로 password 평문)
        User user = userRepository.findUserByCompanyIdAndUserId(defaultCompanyId, username)
                .orElseThrow(() -> {
                    System.out.println("User not found - CompanyId: " + defaultCompanyId + ", Username: " + username);
                    return new UsernameNotFoundException(
                            "User not found with companyId: " + defaultCompanyId + " and username: " + username);
                });
        System.out.println("User found: " + user.getUserId() + ", Full Name: " + user.getUserFullName());

        // 권한 동적 조회
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRoleId() != null) {
            try {
                // roleId로 권한 조회
                List<Role> roles = roleRepository.findRolesByCompanyId(user.getCompanyId());
                Role role = roles.stream()
                        .filter(r -> r.getRoleId().equals(user.getRoleId()) && "Y".equals(r.getUseYn()))
                        .findFirst()
                        .orElse(null);
                if (role != null) {
                    // Role 엔티티에서 권한 정보를 가져오는 로직 (실제 구현에 따라 수정 필요)
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()));
                } else {
                    System.out.println(
                            "Warning: No role found for roleId: " + user.getRoleId() + ", using default role");
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }
            } catch (Exception e) {
                System.out.println(
                        "Warning: Error loading role for roleId: " + user.getRoleId() + ", using default role");
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
        } else {
            // 기본 권한
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        System.out.println("Granted authorities: " + authorities);

        // 회사 정보 조회
        Company company = companyRepository.findCompanyByCompanyId(user.getCompanyId())
                .orElseThrow(() -> new UsernameNotFoundException("Company not found with ID: " + user.getCompanyId()));

        // 부서 정보 조회
        Dept dept = null;
        if (user.getDeptId() != null) {
            try {
                dept = deptRepository.findDeptByCompanyIdAndDeptId(user.getCompanyId(), user.getDeptId())
                        .orElseThrow(() -> new UsernameNotFoundException("Dept not found with ID: " + user.getDeptId()));
            } catch (Exception e) {
                System.out.println(
                        "Warning: Dept not found with ID: " + user.getDeptId() + ", continuing without dept info");
                dept = null;
            }
        }

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getUserId(),
                user.getPassword(),
                authorities,
                user.getCompanyId(),
                company.getCompanyName(),
                user.getDeptId(),
                dept != null ? dept.getDeptName() : "",
                user.getUserFullName());
        System.out.println("Created CustomUserDetails for: " + userDetails.getUsername());
        System.out.println("===============================================");
        return userDetails;
    }
}
