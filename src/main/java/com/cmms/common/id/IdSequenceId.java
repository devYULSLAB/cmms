package com.cmms.common.id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdSequenceId implements Serializable {
    private String companyId;
    private String prefix;
}
