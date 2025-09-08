package com.cmms.plant.repository;

import com.cmms.plant.entity.Plant;
import com.cmms.plant.entity.PlantId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 프로그램명: CMMS 설비 관리 리포지토리
 * 기능: 설비 데이터 조회 및 관리
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-09-04
 */
@Repository
public interface PlantRepository extends JpaRepository<Plant, PlantId> {

    Page<Plant> findPlantsByCompanyId(String companyId, Pageable pageable);

    List<Plant> findPlantsByCompanyId(String companyId);

    List<Plant> findPlantsByCompanyIdAndSiteId(String companyId, String siteId);

    List<Plant> findPlantsByCompanyIdAndFuncId(String companyId, String funcId);
}
