package com.cmms.workpermit.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkpermitId implements Serializable {
    private String companyId;
    private String permitId;
}