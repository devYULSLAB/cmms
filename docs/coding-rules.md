/**
 * 프로그램명: CMMS 코딩 룰
 * 기능: 네이밍/REST/API/DTO/파일업로드/로깅/보안 규칙 표준
 * 생성자: devYULSLAB
 * 생성일: 2025-01-27
 * 변경일: 2025-02-28 (코드 리뷰 반영)
 */

# CMMS 코딩 룰

## 0. 공통
- Stack: Spring Boot 3+, Java 24, Gradle, MariaDB, Thymeleaf, Tailwind
- Locale/Timezone: ko-KR(우선), en / Asia/Seoul 고정
- 로그 MDC: companyId, username, traceId (민감정보 금지)

## 2. 네이밍/엔드포인트
- 화면 메서드: `list, form, editForm, detail, save, update, delete`
- 표준 접근방법 
GET    /inventory              - 목록 조회 (list)
GET    /inventory/new          - 신규 등록 폼 (form)
POST   /inventory              - 신규 등록 실행 (save)
GET    /inventory/{id}         - 상세 조회 (detail)
GET    /inventory/{id}/edit    - 편집 폼 (editForm)
PUT    /inventory/{id}         - 수정 실행 (update)
DELETE /inventory/{id}         - 삭제 실행 (delete)

복합 키 리소스 (Plant 예시)
GET    /plant/{siteId}/{plantId}         - 상세 조회
GET    /plant/{siteId}/{plantId}/edit    - 편집 폼
PUT    /plant/{siteId}/{plantId}         - 수정 실행
DELETE /plant/{siteId}/{plantId}         - 삭제 실행

- API (REST): GET `/api/v1/resources`(page)

## 3. DTO/Controller/Service/Repository
- DTO 접미사: `Request` / `Response` / `Dto`
- Service: `saveXxx`, `updateXxx`, `deleteXxx`, `getXxxBy...`, `listXxxBy...`
- Repository: Spring Data 규칙(find/exists/count/save/delete)
- Service, Repository Layer에서 반환값이 복수형이면 복수형으로 표기하고, 파라미터는 By 뒤에 표기함. 파라미터가 IdClass이면 ById로 표현
  (예) `getDeptsByCompanyId`, `getDeptByCompanyIdAndDeptId`, `getDeptById`

## 4. 파일 업로드 표준
- UX: **파일 선택 즉시 임시 폴더 업로드**(드래그앤드롭 불필요)
- Form(new): 업로드 UI만 / Form(edit): 파일 리스트 + 업로드 / Detail: 리스트만(보기 전용)
- 저장 시: `file_group_id` 발급 → 임시 → 실제 경로로 이동 → `file_attach` 저장
- 경로(권장):
  - 임시: `${app.file.temp-dir}/${sessionId}/`
  - 실제: `${app.file.base-dir}/${company_Id}/${module}/${yyyy}/${MM}/${dd}/${file_group_id}/`

## 5. 로깅/예외/응답
- `@ControllerAdvice` 공통 예외 처리
- API 응답 포맷:
  - 성공: `{\"success\":true,\"data\":{...},\"message\":\"처리 완료\"}`
  - 실패: `{\"success\":false,\"error\":\"메시지\",\"code\":\"ERROR_CODE\"}`
- N+1 방지: `@EntityGraph`/fetch join, 조회 readOnly 트랜잭션

## 6. 세션/보안
- `company_Id`, `user_Id`은 **요청 파라미터 금지**; 세션/보안컨텍스트에서만 주입
- 서버단에서 company_Id 강제 세팅 및 재검증
- View Hidden 필드: `companyId`, `createDate`, `updateDate`
  *(정책상 createBy/updateBy는 사용하지 않음)*
