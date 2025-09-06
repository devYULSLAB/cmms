package com.cmms.common.code.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeId implements Serializable {
    private String companyId;
    private String codeId;
}
