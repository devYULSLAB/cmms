# CMMS 코드 리뷰 보고서

**생성일**: 2025-01-27  
**검토 범위**: 각 모듈별 Entity → Repository → Service → Controller → HTML 순서로 검토

## 🔍 발견된 주요 문제점

### 1. **Import 중복 문제**
- **파일**: `InventoryService.java`, `InventoryController.java`
- **문제**: 동일한 import문이 여러 번 선언됨
- **해결**: 중복된 import문 제거 완료

### 2. **패키지명 불일치 문제**
- **파일**: `PlantMasterRepository.java`, `InventoryStockRepository.java`, `InventoryHistoryRepository.java`
- **문제**: 패키지명이 실제 디렉토리 구조와 불일치
- **해결**: 
  - `com.cmms.plantMaster` → `com.cmms.plant`
  - `com.cmms.inventoryMaster` → `com.cmms.inventory`

### 3. **User 엔티티 필드명 변경**
- **변경사항**: `userName` → `userFullName`
- **영향 범위**: 
  - Entity: `User.java`
  - Service: `CustomUserDetailsService.java`, `UserService.java`
  - Controller: `AuthController.java`
  - Templates: `userList.html`, `userForm.html`

### 4. **Controller 메서드명 불일치**
- **InventoryController**: 
  - `findInventories()` → `getInventoriesByCompanyId()`
  - `findInventoryById()` → `getInventoryById()`
- **PlantController**: 세션 기반 인증 사용 (다른 컨트롤러와 일관성 부족)

### 5. **Repository 메서드명 불일치**
- **InventoryRepository**: `findInventoriesByCompanyId()` 메서드 존재하지만 Controller에서 다른 메서드 호출
- **PlantRepository**: 메서드명이 일관성 있게 구현됨

## ✅ 잘 구현된 부분

### 1. **Entity 구조**
- 모든 Entity가 `@IdClass` 패턴을 올바르게 사용
- `companyId` + `모듈Id` 복합키 구조 일관성 유지
- `fileGroupId` 필드가 모든 Entity에 올바르게 포함

### 2. **Service 계층**
- `@Transactional` 어노테이션 적절히 사용
- ID 생성 로직이 `IdGeneratorService`를 통해 일관성 있게 구현
- 비즈니스 로직과 데이터 접근 로직이 적절히 분리

### 3. **Repository 계층**
- Spring Data JPA 규칙을 올바르게 따름
- 복합키 조회 메서드들이 일관성 있게 구현

## 🔧 권장 수정사항

### 1. **Controller 일관성 개선**
```java
// 현재 (PlantController - 세션 기반)
String companyId = (String) session.getAttribute("companyId");

// 권장 (다른 Controller와 일관성)
@AuthenticationPrincipal CustomUserDetails userDetails
String companyId = userDetails.getCompanyId();
```

### 2. **메서드명 통일**
```java
// InventoryController에서 사용해야 할 메서드명
inventoryService.getInventoriesByCompanyId() // 현재: findInventories()
inventoryService.getInventoryById()          // 현재: findInventoryById()
```

### 3. **예외 처리 개선**
```java
// 현재
.orElseThrow(() -> new RuntimeException("Plant not found: " + plantId));

// 권장
.orElseThrow(() -> new EntityNotFoundException("Plant not found: " + plantId));
```

## 📋 모듈별 상태

| 모듈 | Entity | Repository | Service | Controller | HTML | 상태 |
|------|--------|------------|---------|------------|------|------|
| Plant | ✅ | ✅ | ✅ | ⚠️ | ✅ | 양호 |
| Inventory | ✅ | ✅ | ✅ | ⚠️ | ✅ | 양호 |
| Workorder | ✅ | ✅ | ✅ | ✅ | ✅ | 양호 |
| Workpermit | ✅ | ✅ | ✅ | ✅ | ✅ | 양호 |
| Memo | ✅ | ✅ | ✅ | ✅ | ✅ | 양호 |
| Inspection | ✅ | ✅ | ✅ | ✅ | ✅ | 양호 |
| Workflow | ✅ | ✅ | ✅ | ✅ | ✅ | 양호 |
| Domain | ✅ | ✅ | ✅ | ✅ | ✅ | 양호 |

## 🎯 다음 단계

1. **Controller 메서드명 통일** - 모든 Controller에서 일관된 메서드명 사용
2. **인증 방식 통일** - 모든 Controller에서 `@AuthenticationPrincipal` 사용
3. **예외 처리 개선** - 커스텀 예외 클래스 사용
4. **HTML 템플릿 검토** - 각 모듈별 템플릿 파일 검토 필요

## 📝 코딩 규칙 준수도

- **패키지 구조**: ✅ 준수
- **네이밍 규칙**: ✅ 준수  
- **ID 생성 규칙**: ✅ 준수
- **파일 업로드**: ✅ 준수
- **보안 규칙**: ⚠️ 부분적 준수 (Controller 인증 방식 불일치)
