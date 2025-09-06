package com.cmms.common.code.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeTypeId implements Serializable {
    private String companyId;
    private String codeType;
}
