package com.yulslab.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users") // 'user' is a reserved keyword in some databases
@Getter
@Setter
public class User {

    @Id
    private String userId;

    private String password;

    private String username;

    private String roles; // e.g., "ROLE_USER,ROLE_ADMIN"

    private boolean enabled = true;
}
