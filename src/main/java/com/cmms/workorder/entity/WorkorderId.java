package com.cmms.workorder.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkorderId implements Serializable {
    private String companyId;
    private String workorderId;
}