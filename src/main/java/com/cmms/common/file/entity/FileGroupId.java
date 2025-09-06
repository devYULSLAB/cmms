package com.cmms.common.file.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileGroupId implements Serializable {
    private String companyId;
    private String fileGroupId;
}
