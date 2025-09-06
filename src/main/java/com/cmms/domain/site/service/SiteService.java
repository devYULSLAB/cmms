package com.cmms.domain.site.service;

import com.cmms.domain.site.entity.Site;
import com.cmms.domain.site.entity.SiteId;
import com.cmms.domain.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    @Transactional(readOnly = true)
    public List<Site> findAllByCompanyId(String companyId) {
        return siteRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Site findSiteById(SiteId siteId) {
        return siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));
    }

    public Site saveSite(Site site) {
        return siteRepository.save(site);
    }

    public void deleteSite(SiteId siteId) {
        siteRepository.deleteById(siteId);
    }
}