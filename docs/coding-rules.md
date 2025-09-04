/**
 * 프로그램명: CMMS 코딩 룰 & 구조 가이드
 * 기능: 패키지/네이밍/REST/API/ID발급/파일업로드/i18n/보안 규칙 표준
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-09-04
 */

# CMMS 코딩 룰 & 프로젝트 구조 가이드

## 0. 공통
- Stack: Spring Boot 3+, Java 24, Gradle, MariaDB, Thymeleaf, Tailwind
- Locale/Timezone: ko-KR(우선), en / Asia/Seoul 고정
- 로그 MDC: companyId, username, traceId (민감정보 금지)

## 1. 모듈/패키지
- 루트 그룹: `com.yulslab.cmms` (예: `com.yulslab.cmms.workpermit`)
- 레이어: controller / service / repository / entity / dto / config / mapper
- 화면 Controller: `@Controller` + `/resources` (Thymeleaf)
- API Controller: `@RestController` + `/api/v1/resources` (JSON)
- 공통 디렉터리: `common/{file}` (재사용 컴포넌트)
- Domain 모듈: `domain`(회사/사이트/부서/사용자/권한/ 코드 등 기준정보) **독립 모듈**로 운영

## 2. 네이밍/엔드포인트
- 화면 메서드: `list, form, editForm, detail, save, update, delete`
- 화면 경로(권장):
  - GET `/resources`, `/resources/new`, `/resources/{id}`, `/resources/{id}/edit`
  - POST `/resources`, POST `/resources/{id}` + `_method=PUT`, POST `/resources/{id}/delete`
- API (REST):
  - GET `/api/v1/resources`(page), GET `/{id}`, POST `/`, PUT `/{id}`, DELETE `/{id}`

## 3. DTO/Service/Repository
- DTO 접미사: `Request` / `Response` / `Dto`
- Service: `saveXxx`, `updateXxx`, `deleteXxx`, `getXxxBy...`, `listXxxBy...`
- Repository: Spring Data 규칙(find/exists/count/save/delete)

## 4. 세션/보안
- `company_Id`, `user_Id`은 **요청 파라미터 금지**; 세션/보안컨텍스트에서만 주입
- 서버단에서 company_Id 강제 세팅 및 재검증
- View Hidden 필드: `companyId`, `createDate`, `updateDate`  
  *(정책상 createBy/updateBy는 사용하지 않음)*

## 5. ID 정책 (CHAR(10), 선두자리 prefix)
- 10자리 **숫자 문자열** 고정, **선두 1자리로 모듈 구분**  
  - 1=plant, 2=inventory, 3=inspection, 5=workorder, **7=workflow(결재)**, 9=workpermit 
- 공통 `id_sequence(company_id, prefix, next_val)`에서 **행 잠금** 후 증가값 사용 → **zero-pad** 10자리 생성
- **파일 ID 규칙**
  - **`file_group_id`**: 도메인 레코드(plant/inventory/…/approval 등) 1건당 1개. 해당 도메인의 'FG'+**prefix**를 그대로 사용해 발급
  - **`file_id`**: 그룹에 속한 개별 파일 식별자. 동일하게 10자리/해당 모듈 'FI'+**prefix** 사용
  - 저장 파일명 `stored_name` 권장 포맷: `file_id.ext`

## 6. 코드 관리
- `code_type(company_id, code_type)` / `code(company_id, code_id)` (code_id=5자리)
- 예: JOBTP(작업유형), ASSET(자산유형), DEPRE(감가유형), PERMT(허가유형) / 기능위치, 저장위치는 별도 테이블로 관리 

## 7. 파일 업로드 표준 (단순화)
- UX: **파일 선택 즉시 임시 폴더 업로드**(드래그앤드롭 불필요)
- Form(new): 업로드 UI만 / Form(edit): 파일 리스트 + 업로드 / Detail: 리스트만(보기 전용)
- 저장 시: `file_group_id` 발급 → 임시 → 실제 경로로 이동 → `file_attach` 저장
- 경로(권장):  
  - 임시: `${app.file.temp-dir}/${sessionId}/`  
  - 실제: `${app.file.base-dir}/${company_Id}/${module}/${yyyy}/${MM}/${dd}/${file_group_id}/`

## 8. 템플릿/UI 규칙
- 페이지 제목 `<h2>`, 섹션 제목 `<h5>`
- Form은 섹션별로 구분하고 '저장','목록' 버튼 / List는 각 행별 '수정','삭제' 버튼. id 선택시 Detail로 이동 / Detail은 '출력','목록'버튼. 출력은 layout 제외한 main-content만 
- 버튼:
  - 리스트: `py-1 px-2`(기본) / `px-3`(여유), **배경 없음**(테두리+텍스트)
  - 폼: `py-2 px-4`, Primary/Secondary/Danger 색 구분

## 9. i18n
- `common-core/src/main/resources/i18n/messages_{ko,en}.properties`
- 키 네임스페이스: `cmms.<feature>.<key>`

## 10. 로깅/예외/응답
- `@ControllerAdvice` 공통 예외 처리
- API 응답 포맷:
  - 성공: `{"success":true,"data":{...},"message":"처리 완료"}`
  - 실패: `{"success":false,"error":"메시지","code":"ERROR_CODE"}`
- N+1 방지: `@EntityGraph`/fetch join, 조회 readOnly 트랜잭션
