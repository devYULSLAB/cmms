/**
 * 프로그램명: CMMS 모듈 구조 및 역할 안내
 * 기능: 각 모듈별 역할, 디렉터리/패키지/템플릿 구성 가이드
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-09-04
 */

# CMMS 모듈 구조 및 역할 안내

## 1. 모듈 개요
- **cmms**: plant, inventory, inspection(+item_id 2자리), workorder, workpermit, memo, **workflow(결재)**
- **domain**: 기준정보(회사/사이트/부서/사용자/권한/코드: role, user_role 등) ? **독립 모듈**
- **common-core**: i18n, 공통 유틸/에러/메시지 키
- **common-web**: 레이아웃/공통 프래그먼트(파일 업로드/리스트)
- **common-security**: 인증/인가, CompanyContext/MDC

## 2. 공통(common) 하위 구조
common/
├─ file/
│ ├─ src/main/java/com/yulslab/common/file/...
│ └─ src/main/resources/templates/fragments/
│ ├─ fileUploadFragment.html
│ └─ fileListFragment.html

shell
코드 복사

## 3. domain 모듈 구조(분리)
domain/
└─ src/main/java/com/yulslab/domain/
├─ company/... ├─ site/... ├─ dept/...
├─ user/... └─ role/...
└─ userrole/... └─ codetype/...  └─ code/...
└─ src/main/resources/templates/domain/
├─ company/.html ├─ site/.html ├─ dept/.html
├─ user/.html └─ role/**.html └─ codetype/**.html └─ code/**.html

shell
코드 복사

## 4. cmms 모듈 디렉터리(권장)
cmms/
└─ src/main/
├─ java/com/yulslab/cmms/
│ ├─ plant/{controller,service,repository,entity,dto}
│ ├─ inventory/{controller,service,repository,entity,dto}
│ ├─ inspection/{controller,service,repository,entity,dto}
│ ├─ workorder/{controller,service,repository,entity,dto}
│ ├─ workpermit/{controller,service,repository,entity,dto}
│ ├─ memo/{controller,service,repository,entity,dto}
│ └─ workflow/{controller,service,repository,entity,dto} # 결재
└─ resources/
├─ templates/
│ ├─ layout/defaultLayout.html
│ ├─ plant/{plantList,plantForm,plantDetail}.html
│ ├─ inventory/{inventoryList,inventoryForm,inventoryDetail}.html
│ ├─ inspection/{inspectionList,inspectionForm,inspectionDetail}.html
│ ├─ workorder/{workorderList,workorderForm,workorderDetail}.html
│ ├─ workpermit/{workpermitList,workpermitForm,workpermitDetail}.html
│ ├─ memo/{memoList,memoForm,memoDetail}.html
│ └─ workflow/{approvalList,approvalForm,approvalDetail}.html
└─ db/migration/ # Flyway: V1__cmms_schema.sql ...

markdown
코드 복사

## 5. 컨트롤러 규약 요약
- 화면: `list, form, editForm, detail, save, update, delete`
- API: `/api/v1/<resources>` CRUD 표준, Page 기본
- 파일 필드: 도메인 엔티티에 **`file_group_id (CHAR(10))`** 참조 보관

## 6. UI 규칙 요약
- 제목 계층: 페이지 `<h2>` / 섹션 `<h5>`
- 버튼: 리스트 `py-1 px-2(또는 px-3)` / 폼 `py-2 px-4`, Primary/Secondary/Danger
- 파일 프래그먼트 배치:
  - Form(new): 업로드만
  - Form(edit): 리스트 + 업로드
  - Detail: 리스트만(보기 전용)