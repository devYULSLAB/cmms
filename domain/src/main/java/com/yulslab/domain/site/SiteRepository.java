package com.yulslab.domain.site;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SiteRepository extends JpaRepository<Site, SiteId> {
    List<Site> findById_CompanyIdAndUseYn(String companyId, char useYn);
}
