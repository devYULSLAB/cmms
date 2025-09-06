# 구매 관리 시스템 신규 구축 및 재고 관리 기능 보강 기획안

**문서 버전:** 1.5
**작성일:** 2025-09-05
**작성자:** Jules
**변경 이력:**
- 1.0 ~ 1.3: 이전 변경 이력 생략
- 1.4 (2025-09-05): 재고 조회 기능을 '재고 원장' 기능으로 상세화
- 1.5 (2025-09-05): 월별 재고 마감 기능 추가

---

## 1. 개요
(이전 버전과 동일)

## 2. 목표
(이전 버전과 동일)

## 3. 범위
### 3.1. 포함 범위 (In-Scope)
- **구매 관리 (내부 시스템)**
  (이전 버전과 동일)
- **협력사 포털**
  (이전 버전과 동일)
- **재고 관리 기능 보강**
  - 재고 마스터 유형 관리 (소모품, 부품, 서비스 등)
  - 재고 원장 조회 기능
  - **월별 재고 마감 기능**
- **보고서 및 출력물**
  (이전 버전과 동일)

### 3.2. 미포함 범위 (Out-of-Scope)
(이전 버전과 동일)

---

## 4. 역할별 업무 흐름 (Workflows)
(이전 버전과 동일)

---

## 5. 기술 사양

### 5.1. 데이터베이스 변경 사항
기존 `cmms` 스키마에 아래의 테이블들을 신규로 추가합니다. `inventoryStockByMonth` 테이블이 신규 추가됩니다.

```sql
-- (기존 구매관리 테이블 생략)
CREATE TABLE IF NOT EXISTS supplier ( ... );
-- ... etc ...

-- 월별 재고 마감 테이블
CREATE TABLE IF NOT EXISTS inventoryStockByMonth (
  companyId CHAR(5) NOT NULL,
  siteId CHAR(5) NOT NULL,
  storageId CHAR(5) NOT NULL,
  inventoryId CHAR(10) NOT NULL,
  yyyymm CHAR(6) NOT NULL, -- 'YYYYMM' format
  closing_qty DECIMAL(18,3) NOT NULL,
  closing_total_value DECIMAL(18,2) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (companyId, siteId, storageId, inventoryId, yyyymm),
  CONSTRAINT fk_isbm_site FOREIGN KEY (companyId, siteId) REFERENCES site(companyId, siteId),
  CONSTRAINT fk_isbm_storage FOREIGN KEY (companyId, storageId) REFERENCES storage(companyId, storageId),
  CONSTRAINT fk_isbm_inv FOREIGN KEY (companyId, inventoryId) REFERENCES inventory(companyId, inventoryId)
);
```

### 5.2. 초기 데이터 추가
(이전 버전과 동일)

### 5.3. 애플리케이션 아키텍처
(이전 버전과 동일)

### 5.4. 핵심 프로세스 상세: 입고 처리 및 재고 연동
(이전 버전과 동일)

### 5.5. 핵심 프로세스 상세: 월별 재고 마감
월 단위 재고 마감은 특정 시점의 재고를 확정하는 중요한 배치(batch) 프로세스입니다.

1.  **사용자 마감 실행:** '월별 재고 마감' 화면에서 사용자가 `마감년월(YYYYMM)`, `사이트(필수)`, `창고(선택)`, `품목(선택)`을 지정하고 '마감 실행' 버튼을 클릭합니다.
2.  **직전 월 마감 데이터 확인 (유효성 검증):**
    - 시스템은 마감하려는 년월(`YYYYMM`)의 직전 월 데이터가 `inventoryStockByMonth` 테이블에 존재하는지 확인합니다.
    - **(예외 처리)** 만약 직전 월 데이터가 없다면, 가장 마지막으로 마감된 월 정보를 사용자에게 보여주며 "순차적으로 마감을 진행해야 합니다." 라는 오류 메시지를 반환하고 프로세스를 중단합니다.
3.  **기초 재고 설정:** 검증이 통과되면, 직전 월의 마감 재고(`closing_qty`)를 현재 마감하려는 월의 **기초 재고**로 설정합니다.
4.  **당월 재고 변동량 집계:** `stock_tx` 테이블에서 마감하려는 월(`YYYYMM`)의 모든 재고 변동(입고, 출고, 이동, 조정) 내역을 합산합니다.
5.  **기말 재고 계산:** `기말 재고 = 기초 재고 + 당월 재고 변동량` 공식을 통해 기말 재고를 계산합니다.
6.  **마감 데이터 저장:** 계산된 기말 재고 수량과 금액을 `inventoryStockByMonth` 테이블에 현재 마감 월(`YYYYMM`)의 데이터로 신규 삽입(INSERT)합니다.

---

## 6. 화면 명세 (Screen Specifications)

### 6.1. 내부 관리 시스템
| 화면 ID | 화면명 | 경로 (예시) | 주요 기능 및 내용 |
|---|---|---|---|
| **INV-01** | **재고 원장 조회** | `/inventory/ledger` | - **검색 조건:** 사이트, 창고, 품목, 조회 기간(시작일, 종료일)<br>- **조회 로직:** 선택된 기간 내의 재고 변동(`stock_tx`)을 집계하여 표시<br>- **조회 리스트 헤더:** `회사ID`, `사이트ID`, `창고ID`, `품목ID(+품목명: 15자 축약 및 '...' 표기)`, `기말 재고량`, `기말 재고액`, `기간내 입고`, `기간내 출고`, `기간내 이동`, `기간내 조정`<br>- **출력:** 조회된 재고 원장을 별도의 양식으로 출력하는 기능 |
| **INV-02** | **월별 재고 마감** | `/inventory/closing` | - **마감 범위 지정:** `사이트`(필수), `창고`(선택), `품목`(선택)<br>- **마감 년월 지정:** `YYYYMM` 형식으로 필수 입력<br>- **'마감 실행' 버튼:** 클릭 시 월별 재고 마감 프로세스 실행<br>- 화면 하단에 현재까지 마감된 이력 표시 |
| PUR-01 | 구매 요청 목록 | `/purchase-requests` | - 내가 요청한 구매 건 목록 표시 (상태, 요청일, 제목)<br>- 상태별 필터링, 기간 검색 기능<br>- '신규 요청' 버튼 |
| PUR-02 | 구매 요청 상세/등록 | `/purchase-requests/{id}` | - 구매 요청 정보 표시 및 신규/수정 폼<br>- (승인 완료 시) 요청서 표지 및 상세 내역 출력 기능 |
| ... (이하 이전 버전과 동일) ... |

### 6.2. 협력사 포털
(이전 버전과 동일)

---

## 7. 신규 보고서
1.  **재고 원장 보고서 (Inventory Ledger Report):** `사이트`, `창고`, `품목`, `조회 기간`을 기준으로 재고의 상세 변동 이력(입고, 출고, 이동, 조정)과 기말 재고를 집계하여 조회합니다. `품목명`은 15자로 축약하여 표시하며, 조회된 내용은 별도의 출력 양식으로 인쇄 가능합니다.
2.  **월별 재고 마감 보고서:** `inventoryStockByMonth` 테이블의 데이터를 기반으로, 월별/사이트별/창고별 마감 재고 현황을 조회합니다.
3.  **구매 이력 보고서:** 특정 기간 동안의 구매 오더 내역을 협력사별, 품목별로 집계하여 보여줍니다.
4.  **협력사 평가 보고서:** 협력사별 평가 점수(품질, 납기 등)를 종합하여 리포트합니다.
