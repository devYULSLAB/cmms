package com.cmms.storage.service;

import com.cmms.storage.entity.Storage;
import com.cmms.storage.entity.StorageId;
import com.cmms.storage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;

    @Transactional(readOnly = true)
    public List<Storage> findAllByCompanyId(String companyId) {
        return storageRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Storage findStorageById(StorageId id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found"));
    }

    public Storage saveStorage(Storage storage) {
        return storageRepository.save(storage);
    }

    public void deleteStorage(StorageId id) {
        storageRepository.deleteById(id);
    }
}