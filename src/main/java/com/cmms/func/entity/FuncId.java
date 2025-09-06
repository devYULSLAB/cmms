package com.cmms.func.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncId implements Serializable {
    private String companyId;
    private String funcId;
}
