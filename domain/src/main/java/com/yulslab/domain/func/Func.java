package com.yulslab.domain.func;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "func")
@Getter
@Setter
public class Func {
    @EmbeddedId
    private FuncId id;
    private String funcName;
    private String parentFuncId;
    private char useYn;
    private int sortOrder;
}
