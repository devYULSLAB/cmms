package com.yulslab.domain.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StorageRepository extends JpaRepository<Storage, StorageId> {
    List<Storage> findById_CompanyIdAndUseYn(String companyId, char useYn);
}
