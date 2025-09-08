/**
 * 프로그램명: CMMS 모듈 구조 가이드
 * 기능: 모듈/패키지, 디렉터리, 환경설정, DB, ID/코드, 로그인 처리 정리
 * 생성자: devYULSLAB
 * 생성일: 2025-01-27
 * 변경일: 2025-03-01 (도메인 기반 companyId 주입 제안)
 */

# CMMS 프로젝트 구조 가이드

## 1. 모듈/패키지
- 루트 그룹: `com.cmms` (예: `com.cmms.workpermit`)
- 레이어: controller / service / repository / entity / dto / config / mapper
- 화면 Controller: `@Controller` + `/resources` (Thymeleaf)
- API Controller: `@RestController` + `/api/v1/resources` (JSON)
- 공통 디렉터리: `common/{file}` (재사용 컴포넌트)
- Domain 모듈: `domain`(회사/사이트/부서/사용자/권한/코드 등 기준정보) **독립 모듈**로 운영

## 2. 디렉토리 구조
```
src/main/java/com/cmms/
  ├─ plant/{controller,service,repository,entity,dto}
      ├─ func/
  ├─ inventory/{controller,service,repository,entity,dto}
      ├─ storage/
  ├─ inspection/{controller,service,repository,entity,dto}
  ├─ workorder/{controller,service,repository,entity,dto}
  ├─ workpermit/{controller,service,repository,entity,dto}
  ├─ memo/{controller,service,repository,entity,dto}
  ├─ workflow/{controller,service,repository,entity,dto}
  ├─ domain/
      ├─ {company,site,dept,user,role,use_role}
  ├─ common/
      ├─ {file, code, codeType}
src/main/resources/templates
  ├─ auth/Login.html
  ├─ layout/defaultLayout.html
  ├─ {plant,inventory...}
src/main/resources/messages
```

## 3. application.yml
- `application.yml`, `application-dev.yml`, `application-prod.yml`로 구분
- 데이터베이스 및 파일 업로드 설정은 환경변수 또는 외부 설정 사용
- `cmms.default-company-id` 기본값: `${DEFAULT_COMPANY_ID:C0001}`

## 4. 데이터베이스 설정
- MariaDB 사용, 드라이버 `org.mariadb.jdbc.Driver`
- 초기 스키마 및 시드 데이터: `db/cmms-schema-migration.sql`

## 5. ID 정책 및 코드 관리
- ID 정책 (CHAR(10), 선두자리 prefix)
  - 0=memo, 1=plant, 2=inventory, 3=inspection, 5=workorder, 7=stock, 8=stock tx, 9=workpermit
  - 공통 `id_sequence(company_id, prefix, next_val)`에서 **행 잠금** 후 증가값 사용 → **zero-pad** 10자리 생성
  - 파일 ID
    - `file_group_id`: 도메인 레코드 1건당 1개, 'FG'+'8자리 숫자:밀리초 기반'
    - `file_id`: 그룹 내 개별 파일 식별자, 'FI'+'8자리 숫자'
    - 저장 파일명 `stored_name` 권장: `file_id.ext`
  - 결재 ID
    - `approval_Id`: 'WF' + '8자리 숫자:밀리초 기반'
    - `template_Id`: 'WT' + '8자리 숫자:밀리초 기반'
- 코드 관리
  - `code_type(company_id, code_type)` / `code(company_id, code_id)` (code_id=5자리)
  - 예: JOBTP(작업유형), ASSET(자산유형), DEPRE(감가유형), PERMT(허가유형)

## 6. 로그인 처리
- 로기인 페이지는 layout을 포함하지 않는 독립적인 단독 페이지 
- `company_Id`는 `application.yml`의 `default-company-id` (`C0001`) 사용
- Login.html에서 `companyId`를 받지 않고 `userId`, `password`만 입력받아 기본 회사코드를 주입
- 추후 멀티 테넌트 확장을 위해 `CompanyIdResolver` 인터페이스 제공, 구현 교체로 대응 가능
- 도메인별 회사 구분이 필요한 경우:
  - `Host` 헤더 또는 서브도메인에서 회사 식별자를 추출하는 `DomainCompanyIdResolver` 구현
  - 매핑 정보는 DB(`domain_company`) 또는 `application.yml`의 `domain-company-map`에 정의 후 캐싱
  - 로그인 필터나 컨트롤러에서 `CompanyIdResolver`를 주입받아 `companyId` 결정
