package com.yulslab.domain.storage;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class StorageId implements Serializable {
    private String companyId;
    private String storageId;
}
