package com.yulslab.domain.code;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "code_type")
@Getter
@Setter
public class CodeType {
    @EmbeddedId
    private CodeTypeId id;
    private String codeTypeName;
    private char useYn;
    private int sortOrder;
}
