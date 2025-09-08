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
    public List<Site> getSitesByCompanyId(String companyId) {
        return siteRepository.findSitesByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Site getSiteById(SiteId siteId) {
        return siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));
    }

    @Transactional(readOnly = true)
    public Site getSiteByCompanyIdAndSiteId(String companyId, String siteId) {
        return siteRepository.findSiteByCompanyIdAndSiteId(companyId, siteId)
                .orElseThrow(() -> new RuntimeException("Site not found: " + siteId));
    }

    public Site saveSite(Site site) {
        return siteRepository.save(site);
    }

    public void deleteSite(SiteId siteId) {
        siteRepository.deleteById(siteId);
    }
}