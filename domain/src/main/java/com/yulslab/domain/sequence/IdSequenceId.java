package com.yulslab.domain.sequence;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class IdSequenceId implements Serializable {
    private String companyId;
    private char prefix;
}
