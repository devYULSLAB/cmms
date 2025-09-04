# CMMS (Computerized Maintenance Management System)

CMMS는 설비 유지보수 관리를 위한 웹 기반 시스템입니다.

## 📋 프로젝트 개요

- **기술 스택**: Spring Boot 3+, Java 24, Gradle, MariaDB, Thymeleaf, Tailwind CSS
- **개발 환경**: Windows
- **운영 환경**: Linux
- **로케일**: ko-KR (우선), en / Asia/Seoul 고정

## 🏗️ 프로젝트 구조

```
cmms/
├── docs/                    # 프로젝트 문서
│   ├── coding-rules.md      # 코딩 규칙 및 구조 가이드
│   ├── module-structure.md  # 모듈 구조 및 역할 안내
│   └── ui-layout-guide.md   # UI 표준 레이아웃 가이드
├── db/                      # 데이터베이스
│   └── cmms-schema-migration.sql  # 스키마 생성 및 초기 데이터
└── README.md               # 프로젝트 개요
```

## 📚 상세 문서

프로젝트의 상세한 가이드와 규칙은 다음 문서들을 참조하세요:

- **[코딩 규칙 및 구조 가이드](docs/coding-rules.md)** - 패키지/네이밍/REST/API/ID발급/파일업로드/i18n/보안 규칙
- **[모듈 구조 및 역할 안내](docs/module-structure.md)** - 각 모듈별 역할, 디렉터리/패키지/템플릿 구성
- **[UI 표준 레이아웃 가이드](docs/ui-layout-guide.md)** - 레이아웃/리스트/폼/버튼/섹션/파일프래그먼트 사용 규칙

## 🗄️ 데이터베이스

데이터베이스 스키마 및 초기 데이터는 `db/cmms-schema-migration.sql` 파일을 참조하세요.

## 🔧 주요 모듈

- **plant**: 설비 관리
- **inventory**: 재고 관리  
- **inspection**: 점검 관리
- **workorder**: 작업 지시 관리
- **workpermit**: 작업 허가 관리
- **workflow**: 결재 관리
- **memo**: 메모 관리
- **domain**: 기준정보 (회사/사이트/부서/사용자/권한/코드) - 독립 모듈

## 📝 개발 가이드

자세한 개발 가이드와 코딩 규칙은 `docs/` 폴더의 문서들을 참조하시기 바랍니다. 
