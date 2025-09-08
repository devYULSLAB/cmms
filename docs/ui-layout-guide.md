/**
 * 프로그램명: CMMS UI 표준 레이아웃 가이드
 * 기능: 레이아웃/리스트/폼/버튼/섹션/파일프래그먼트 사용 규칙
 * 생성자: devYULSLAB
 * 생성일: 2025-01-27
 * 변경일: 2025-09-04
 */

# CMMS UI 표준 레이아웃 가이드

## 1. 레이아웃(Thymeleaf + Tailwind)
- 기본 템플릿: `layout/defaultLayout.html`
- 영역:
  - Header: 좌측 로고/시스템명, 우측 로그인 사용자 정보 + **설정(톱니바퀴)**
  - Left Sidebar: 메뉴/모듈 트리
  - Main: `layout:fragment="content"` , `"layout:fragment="page-js"`
  - Footer: 간단한 카피/버전
- **제목 규칙**: 페이지 제목 `<h2>`, 섹션 제목 `<h5>`
- 사용자 정보 클릭 → 요약/로그아웃, 톱니바퀴 → 프로필/비밀번호 변경

## 2. 공통 폭/간격/타이포
- 컨테이너: `mx-auto px-4`
- 리스트: `max-w-6xl`
- 폼: `max-w-4xl`
- 섹션: `<h5>` + 카드(`rounded-2xl shadow p-4 md:p-6 mb-6`)
- 타이포:
  - `<h2>`: `text-2xl md:text-3xl font-semibold text-gray-900`
  - `<h5>`: `text-base md:text-lg font-semibold text-gray-800`

## 3. 버튼/색상(확정)
### 3.1 리스트용 버튼(수정/삭제 등)
- 크기: **`py-1 px-2`**(기본), 여유 시 `px-3`
- 스타일: **배경 없음**, 테두리+텍스트  
  - 예) `inline-flex items-center rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50`

### 3.2 폼용 버튼(신규/저장 등)
- 크기: **`py-2 px-4`**
- Primary: `bg-blue-600 hover:bg-blue-700 text-white`
- Secondary: `bg-gray-200 hover:bg-gray-300 text-gray-900`
- Danger: `bg-red-600 hover:bg-red-700 text-white`
- 공통: `inline-flex items-center rounded-md font-medium transition`

### 3.3 배치
- 액션바 우상단 정렬, 목록/상세/편집/저장/삭제 일관 배치

## 4. 리스트 표준
- 헤더 굵게(가능 시 sticky)
- 페이징: 서버 Page 기반(크기 선택 + 검색 바)
- 필터: 키워드/코드/날짜 범위

## 5. 폼 표준
- 섹션 예: Basic Info / 제조사 정보 / 메모 & 파일
- Hidden: companyId, createDate, updateDate
- 검증 메시지: 서버/클라이언트 i18n 일치

## 6. 파일 프래그먼트
- Form(new): `fileUploadFragment`만
- Form(edit): `fileListFragment` + `fileUploadFragment`
- Detail: `fileListFragment`만(보기 전용)
- **표시 식별자**: 리스트/상세 모두 **`file_group_id`** 기준으로 조회/표시  
  (단일 파일 단위 조작 시 **`file_id`** 기반)

## 7. 조직도(선택)
- 1차: 목록/검색, 추후 트리 뷰 확장 가능

## 8. 접근성
- 포커스 링 유지, ARIA 라벨, 대체 텍스트
