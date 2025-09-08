package com.cmms.storage.repository;

import com.cmms.storage.entity.Storage;
import com.cmms.storage.entity.StorageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, StorageId> {

    List<Storage> findStoragesByCompanyId(String companyId);
}