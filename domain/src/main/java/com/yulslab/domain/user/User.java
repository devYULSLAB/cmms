package com.yulslab.domain.user;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "`user`") // Use backticks to handle reserved keyword
@Getter
@Setter
public class User {

    @EmbeddedId
    private UserId id;

    private String userName;

    private String passwordHash;

    private LocalDateTime passwordUpdatedAt;

    private int failedLoginCount;

    private char isLocked;

    private LocalDateTime lastLoginAt;

    private char mustChangePw;

    // Note: 'roles' are managed in a separate user_role table, so not included here as a simple field.
    // A @OneToMany relationship would be more appropriate if role management is needed within the entity.
}
