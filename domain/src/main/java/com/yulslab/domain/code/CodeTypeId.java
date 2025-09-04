package com.yulslab.domain.code;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class CodeTypeId implements Serializable {
    private String companyId;
    private String codeType;
}
