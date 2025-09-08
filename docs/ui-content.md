/**
 * 프로그램명: CMMS UI 컨텐츠 가이드
 * 기능: Plant/Inventory/Inspection/Workorder/Workpermit/Memo/Workflow/Domain/CodeType/Code UI 필드 정의 및 공통 규칙
 * 생성자: devYULSLAB
 * 생성일: 2025-01-27
 * 변경일: 2025-03-01 (ui-content-guide 통합)
 */

# CMMS UI 컨텐츠 가이드

모듈별 UI 규격:

- [설비 (Plant)](#plant-설비)
- [품목 (Inventory)](#inventory-품목)
- [점검 (Inspection)](#inspection-점검)
- [작업지시 (Workorder)](#workorder-작업지시)
- [작업허가 (Workpermit)](#workpermit-작업허가)
- [게시판 (Memo)](#memo-게시판)
- [결재 (Workflow)](#workflow-결재)
- [기준정보 (Domain)](#domain)
- [코드타입 (CodeType)](#codetype)
- [코드 (Code)](#code)

## 공통 UI 규칙
- ID는 자동발번 필드는 Form에서 readonly(생성 시 서버발번 후 바인딩).
- Select 소스: code/use_yn='Y' 정렬, 부서/기능/유저는 회사(및 사이트) 필터 반영.
- 파일 업로드: file_group_id 자동 생성 후 바인딩.
- 상태값: Inspection/Workorder/Workpermit는 T(임시)→C(확정) 전환 시 수정 제한(관리자만 롤백).
- List 기본 동작: 검색(키워드/기간/상태), 정렬, 페이징, 내보내기(CSV/XLSX) 옵션.

# Plant (설비)

Form/Detail 섹션
1) Basic information

[company_id] (회사ID / hidden / 기본값 주입)

[site_id] (사이트 / select / 필수)

[plant_id] (설비ID / text-readonly / 10자리, 선두 1 자동발번)

[plant_name] (설비명 / text / 필수)

[master_type] (자산유형 / select / code_type=ASSET)

[func_id] (기능위치 / select / func)

[dept_id] (관리부서 / select / dept)

2) Financial information

[install_date] (설치일 / date)

[depre_type] (감가유형 / select / code_type=DEPRE)

[depre_period] (내용연수(년) / number)

[acquire_cost] (취득가액 / number(18,2))

[residual_value] (잔존가액 / number(18,2))

3) Maker information

[maker_name] (제조사 / text)

[spec] (규격/사양 / text)

[model_no] (모델번호 / text)

[serial_no] (시리얼번호 / text)

4) Operational Flags

[preventive_yn] (예방정비 대상 / checkbox)

[psm_yn] (PSM 대상 / checkbox)

[wp_target_yn] (작업허가 대상 / checkbox)

5) Note, File

[notes] (비고 / textarea)

[file_group_id] (파일그룹ID / hidden / 최초 저장 시 생성)

6) System

[create_date], [update_date] (생성/수정일시 / readonly)

List 컬럼

회사ID, 사이트, 설비ID, 설비명, 자산유형, 기능위치, 관리부서, 설치일, 예방/PSM/WP 플래그, 수정일

# Inventory (품목)

Form/Detail 섹션
1) Basic information

[company_id] (회사ID / hidden)

[inventory_id] (품목ID / text-readonly / 10자리, 선두 2 자동발번)

[inventory_name] (품목명 / text / 필수)

[master_type] (유형 / select / code_type=ASSET)

[dept_id] (관리부서 / select / dept)

2) Maker information

[maker_name], [spec], [model_no], [serial_no] (text)

3) Note, File

[notes] (textarea)

[file_group_id] (hidden)

4) System

[create_date], [update_date] (readonly)

List 컬럼

회사ID, 품목ID, 품목명, 유형, 관리부서, 제조사, 규격, 수정일

# Inspection (점검)

Form/Detail 섹션
1) Basic information

[company_id] (hidden), [site_id] (select)

[inspection_id] (점검ID / text-readonly / 선두 3)

[inspection_name] (점검명 / text / 필수)

[plant_id] (설비 / select / plant@site)

[job_type] (작업유형 / select / code_type=JOBTP)

[dept_id] (담당부서 / select)

2) Schedule

[planned_date] (계획일시 / datetime)

[actual_date] (실시일시 / datetime)

[status] (상태 / radio: T(임시), C(확정))

3) Items (그리드/반복행)

[item_id] (2자리 / 자동 or 수동)

[item_name] (점검항목명 / text)

[method] (방법 / text)

[unit] (단위 / text)

[min_val], [max_val], [std_val] (number(18,4))

[result_val] (number(18,4))

[notes] (text)

4) Note, File

[notes] (점검 비고 / textarea) — inspection 테이블 필드

[file_group_id] (hidden)

5) System

[create_date], [update_date] (readonly)

List 컬럼

회사ID, 사이트, 점검ID, 점검명, 설비, 작업유형, 계획일시, 실시일시, 상태, 수정일

# Workorder (작업지시)

Form/Detail 섹션
1) Basic information

[company_id] (hidden), [site_id] (select)

[workorder_id] (작업지시ID / text-readonly / 선두 5)

[workorder_name] (작업명 / text / 필수)

[plant_id] (설비 / select)

[job_type] (작업유형 / select / JOBTP)

2) Schedule

[planned_date] (계획일 / date)

[actual_date] (실시일 / date)

[status] (상태 / radio: T/C)

3) Cost/Time

[planned_cost], [actual_cost] (number(18,2))

[planned_time], [actual_time] (number(18,2) / 인시 등)

4) Items (그리드/반복행)

[item_id] (2자리)

[item_name] (작업항목명)

[method] (방법)

[result_value] (결과값 / varchar)

[part_inventory_id] (사용부품 / select: inventory)

[qty] (수량 / number(18,3)), [uom] (단위 / text)

5) Note, File

[notes] (textarea)

[file_group_id] (hidden)

6) System

[create_date], [update_date] (readonly)

List 컬럼

회사ID, 사이트, 작업지시ID, 작업명, 설비, 작업유형, 계획일, 실시일, 상태, 계획/실적비용, 수정일

# Workpermit (작업허가)

정책: permit_id는 회사 단위 유일(PK: company_id+permit_id)

Form/Detail 섹션
1) Basic information

[company_id] (hidden), [site_id] (select)

[permit_id] (허가ID / text-readonly / 선두 9)

[permit_name] (허가명 / text / 필수)

[permit_type] (허가유형 / select / code_type=PERMT)

[workorder_id] (작업지시 / select)

[plant_id] (설비 / select)

[dept_id] (부서 / select)

2) Schedule

[start_date] (시작일 / date)

[end_date] (종료일 / date)

[status] (상태 / radio: T/C)

3) Safety Information

[work_summary] (작업개요 / text)

[hazard_factor] (위험요인 / text)

[safety_factor] (안전조치 / text)

4) Items (체크리스트/서명)

[item_id] (2자리)

[item_name] (항목명)

[signature] (서명 식별자/파일ID 등)

[name] (서명자명)

5) Note, File

[notes] (textarea)

[file_group_id] (hidden)

6) System

[create_date], [update_date] (readonly)

List 컬럼

회사ID, 사이트, 허가ID, 허가명, 허가유형, 작업지시, 설비, 기간(시작~종료), 상태, 수정일

# Memo (게시판)

Form/Detail 섹션
1) Basic information

[company_id] (hidden)

[memo_id] (메모ID / text-readonly / 선두 8)

[memo_name] (제목 / text / 필수)

[isPinned] (상단고정 / checkbox)

[author_id] (작성자 / select: user)

2) Content

[content] (본문 / richtext or textarea)

3) Files

[file_group_id] (hidden) + 파일업로드 컴포넌트

4) Comments (반복행)

[comment_id] (코멘트ID / text-readonly / 10자리)

[author_id] (작성자 / select)

[content] (코멘트 내용 / textarea)

5) System

[view_cnt] (조회수 / readonly)

[create_date], [update_date] (readonly)

List 컬럼

회사ID, 메모ID, 제목, 고정여부, 작성자, 조회수, 수정일

# Workflow (결재)

## Template (결재템플릿)
Form/Detail 섹션
1) Basic information

[company_id] (hidden)

[template_id] (템플릿ID / text-readonly / 선두 7)

[template_name] (템플릿명 / text / 필수)

[description] (설명 / textarea)

[use_yn] (사용여부 / checkbox)

2) Steps (반복행)

[step_no] (단계번호 / number / 필수)

[role_id] (결재역할 / select: role)

[approver_user] (지정결재자 / select: user / 선택)

[condition_expr] (조건식 / text / 선택)

[sort_order] (정렬 / number)

3) System

[create_date], [update_date] (readonly)

List 컬럼

회사ID, 템플릿ID, 템플릿명, 사용여부, 단계수, 수정일

## Request (결재요청)
Form/Detail 섹션
1) Basic information

[company_id] (hidden)

[approval_id] (결재ID / text-readonly / 선두 7)

[template_id] (템플릿 / select)

[title] (제목 / text / 필수)

[requester_user] (상신자 / select: user / 기본 로그인 사용자)

[status] (상태 / radio: D/S/P/A/R)

2) Steps(진행) — 읽기 중심/결정 입력

[step_no] (단계번호 / number / readonly)

[approver_user] (결재자 / select / readonly or 표시용)

[decision] (결정 / radio: W/A/R)

[decided_at] (결정일시 / datetime / readonly or 자동)

[comment] (의견 / textarea)

3) Files & Notes

[file_group_id] (hidden)

[notes] (textarea)

4) System

[create_date], [update_date] (readonly)

List 컬럼

회사ID, 결재ID, 제목, 템플릿, 상신자, 상태, 수정일

# Domain

## Company
Form/Detail

[company_id] (회사ID / text / PK)

[company_name] (회사명 / text / 필수)

[use_yn] (사용여부 / checkbox)

[create_date], [update_date] (readonly)

List

회사ID, 회사명, 사용여부, 수정일

## Site
Form/Detail

[company_id] (hidden)

[site_id] (사이트ID / text / PK)

[site_name] (사이트명 / text / 필수)

[use_yn] (checkbox)

[create_date], [update_date] (readonly)

List

회사ID, 사이트ID, 사이트명, 사용여부, 수정일

## Dept
Form/Detail

[company_id] (hidden)

[dept_id] (부서ID / text / PK)

[dept_name] (부서명 / text / 필수)

[use_yn] (checkbox)

[create_date], [update_date] (readonly)

List

회사ID, 부서ID, 부서명, 사용여부, 수정일

## User
Form/Detail

[company_id] (hidden)

[user_id] (사용자ID / text / PK)

[user_name] (사용자명 / text / 필수)

[password_hash] (비밀번호 / password / 생성 시만 입력)

[password_updated_at] (비번변경일 / readonly)

[failed_login_count] (실패횟수 / readonly)

[is_locked] (잠금 / toggle)

[last_login_at] (마지막로그인 / readonly)

[must_change_pw] (최초변경필수 / toggle)

[create_date], [update_date] (readonly)

List

회사ID, 사용자ID, 사용자명, 잠금, 실패횟수, 마지막로그인, 수정일

## UserRole (사용자-권한)
Form/Detail

[company_id] (hidden)

[user_id] (사용자 / select: user)

[role_id] (권한 / select: role)

[grant_date] (부여일 / readonly)

PK: (company_id, user_id, role_id) — 동일 조합 중복 방지

List

회사ID, 사용자ID, 사용자명, 권한ID, 권한명, 부여일

# CodeType

Form/Detail

[company_id] (hidden)

[code_type] (코드타입 / text / PK)

[code_type_name] (코드타입명 / text / 필수)

[use_yn] (checkbox)

[sort_order] (정렬 / number)

[create_date], [update_date] (readonly)

List

회사ID, 코드타입, 코드타입명, 사용여부, 정렬, 수정일

# Code

Form/Detail

[company_id] (hidden)

[code_id] (코드ID / text / 5자)

[code_type] (코드타입 / select: code_type)

[code_name] (코드명 / text / 필수)

[use_yn] (checkbox)

[sort_order] (정렬 / number)

[create_date], [update_date] (readonly)

PK: (company_id, code_id)

List

회사ID, 코드ID, 코드명, 코드타입, 사용여부, 정렬, 수정일
