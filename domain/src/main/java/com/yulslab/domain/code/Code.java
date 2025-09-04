package com.yulslab.domain.code;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "code")
@Getter
@Setter
public class Code {
    @EmbeddedId
    private CodeId id;
    private String codeType;
    private String codeName;
    private char useYn;
    private int sortOrder;
}
