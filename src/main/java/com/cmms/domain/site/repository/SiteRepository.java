package com.cmms.domain.site.repository;

import com.cmms.domain.site.entity.Site;
import com.cmms.domain.site.entity.SiteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site, SiteId> {

    List<Site> findByCompanyId(String companyId);
}
