package com.yulslab.domain.storage;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "storage")
@Getter
@Setter
public class Storage {
    @EmbeddedId
    private StorageId id;
    private String storageName;
    private String parentStorageId;
    private char useYn;
    private int sortOrder;
}
