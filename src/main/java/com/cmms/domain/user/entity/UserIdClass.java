package com.cmms.domain.user.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdClass implements Serializable {
    private String companyId;
    private String userId;
}
