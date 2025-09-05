# 구매 관리 시스템 신규 구축 및 재고 관리 기능 보강 기획안

**문서 버전:** 1.0
**작성일:** 2025-09-05
**작성자:** Jules

---

## 1. 개요
본 문서는 기존 CMMS 시스템의 기능을 확장하여, 체계적인 구매 관리 프로세스를 신규 도입하고 기존 재고 관리 기능을 보강하기 위한 기획안을 제시합니다. 본 기획안은 기존 시스템의 설계 문서(MD, SQL)를 기반으로 데이터와 기능의 일관성을 유지하며, 사용자 편의성과 업무 효율성을 높이는 것을 목표로 합니다.

## 2. 목표
- **구매 프로세스 표준화:** 구매 요청부터 발주, 입고, 평가에 이르는 전 과정을 시스템화하여 업무 누락을 방지하고 투명성을 확보합니다.
- **협력사 관리 효율화:** 협력사 정보를 통합 관리하고, 온라인 포털을 통해 협력사의 입찰 참여 및 정보 조회를 지원하여 소통 비용을 절감합니다.
- **재고 관리 고도화:** 재고 품목의 유형(마스터) 관리를 강화하고, 실시간 재고 현황 및 입출고 이력 리포트를 제공하여 정확한 재고 관리를 지원합니다.
- **데이터 연동 및 통합:** 구매(입고)와 재고(수량 증가) 데이터를 실시간으로 연동하여 데이터 정합성을 확보합니다.

## 3. 범위
### 3.1. 포함 범위 (In-Scope)
- **구매 관리 (내부 시스템)**
  - 구매 요청 생성 및 승인
  - 협력사 정보 등록 및 관리
  - 입찰 공고 등록 및 관리
  - 협력사별 투찰 내용 확인
  - 구매 오더(PO) 생성 및 관리
  - 물품 입고 처리
  - 납품 완료 건에 대한 협력사 평가
- **협력사 포털**
  - 협력사 회원가입 및 로그인
  - 입찰 공고 조회
  - 입찰 참여(투찰) 및 결과 조회
- **재고 관리 기능 보강**
  - 재고 마스터 유형 관리 (소모품, 부품, 서비스 등)
  - 사이트 및 저장 위치(Storage)별 재고 현황 리포트
  - 재고 품목별 입/출고 이력 리포트
- **보고서**
  - 구매 이력 보고서
  - 협력사 평가 보고서
  - 재고 현황 및 이력 보고서

### 3.2. 미포함 범위 (Out-of-Scope)
- 회계 시스템 연동 (비용 처리, 세금 계산서 발행 등)
- 전자 계약 시스템
- 수요 예측 기능

---

## 4. 역할별 업무 흐름 (Workflows)

### 4.1. 현업 담당자 (요청자)
1.  **구매 요청:** 필요한 물품/서비스 발생 시, 시스템에서 '구매 요청서' 작성 및 제출.
2.  **입고 확인:** 요청한 물품 도착 시, '인수 확인' 처리.

### 4.2. 구매 담당자
1.  **요청 검토:** 접수된 구매 요청 검토 및 승인/반려.
2.  **협력사 관리:** 신규 협력사 등록 및 정보 관리.
3.  **입찰 공고:** 필요 시 '입찰 공고' 등록.
4.  **낙찰 및 업체 선정:** 투찰 결과 비교 후 협력사 선정.
5.  **구매 오더(PO) 생성:** 선정된 협력사에 '구매 오더' 생성 및 발송.
6.  **납품 관리:** 납품 진행 상황 모니터링.
7.  **협력사 평가:** 납품 완료 건에 대해 품질, 납기 등 평가.

### 4.3. 재고 담당자
1.  **입고 처리:** 납품된 물품을 PO와 대조하여 검수.
2.  **재고 등록:** 검수 완료 물품을 시스템에 '입고' 등록. 이 때, **자동으로 재고 수량이 증가**하고 **입고 이력(Transaction)이 생성**됨.
3.  **재고 관리:** 재고 실사, 재고 현황 및 이력 리포트 조회/관리.

### 4.4. 협력사 (포털 이용)
1.  **가입 및 로그인:** 협력사 포털에 가입 및 로그인.
2.  **공고 조회:** 신규 입찰 공고 조회.
3.  **투찰:** 입찰 참여 및 견적 제출.
4.  **결과 조회:** 투찰 결과 확인.

---

## 5. 기술 사양

### 5.1. 데이터베이스 변경 사항
기존 `cmms` 스키마에 아래의 테이블들을 신규로 추가합니다.

```sql
-- =========================================================
-- 11) 구매 관리 (Purchase Management)
-- =========================================================

-- 협력사
CREATE TABLE IF NOT EXISTS supplier (
  company_id CHAR(5) NOT NULL,
  supplier_id CHAR(10) NOT NULL, -- 선두 4
  supplier_name VARCHAR(100) NOT NULL,
  business_no VARCHAR(20) NULL,
  contact_person VARCHAR(50) NULL,
  email VARCHAR(100) NULL,
  password_hash VARCHAR(100) NULL, -- Portal login
  address VARCHAR(200) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, supplier_id),
  CONSTRAINT fk_supplier_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- 구매요청
CREATE TABLE IF NOT EXISTS purchase_request (
  company_id CHAR(5) NOT NULL,
  pr_id CHAR(10) NOT NULL, -- 선두 4
  requester_user_id CHAR(5) NOT NULL,
  request_dept_id CHAR(5) NULL,
  request_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED', -- REQUESTED, APPROVED, ORDERED, REJECTED
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, pr_id),
  CONSTRAINT fk_pr_requester FOREIGN KEY (company_id, requester_user_id) REFERENCES `user`(company_id, user_id),
  CONSTRAINT fk_pr_dept FOREIGN KEY (company_id, request_dept_id) REFERENCES dept(company_id, dept_id)
);

CREATE TABLE IF NOT EXISTS purchase_request_item (
  company_id CHAR(5) NOT NULL,
  pr_item_id BIGINT AUTO_INCREMENT NOT NULL,
  pr_id CHAR(10) NOT NULL,
  inventory_id CHAR(10) NULL, -- 재고 마스터와 연동
  item_name VARCHAR(100) NOT NULL, -- 재고 마스터에 없는 경우 직접 입력
  quantity DECIMAL(18,3) NOT NULL,
  uom VARCHAR(20) NULL,
  PRIMARY KEY (company_id, pr_item_id),
  CONSTRAINT fk_pri_pr FOREIGN KEY (company_id, pr_id) REFERENCES purchase_request(company_id, pr_id) ON DELETE CASCADE,
  CONSTRAINT fk_pri_inv FOREIGN KEY (company_id, inventory_id) REFERENCES inventory(company_id, inventory_id)
);

-- 입찰공고
CREATE TABLE IF NOT EXISTS tender (
  company_id CHAR(5) NOT NULL,
  tender_id CHAR(10) NOT NULL, -- 선두 4
  tender_name VARCHAR(150) NOT NULL,
  start_date DATETIME NOT NULL,
  end_date DATETIME NOT NULL,
  description TEXT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ANNOUNCED', -- ANNOUNCED, CLOSED, AWARDED
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, tender_id),
  CONSTRAINT fk_tender_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS tender_item (
  company_id CHAR(5) NOT NULL,
  tender_item_id BIGINT AUTO_INCREMENT NOT NULL,
  tender_id CHAR(10) NOT NULL,
  inventory_id CHAR(10) NULL,
  item_name VARCHAR(100) NOT NULL,
  quantity DECIMAL(18,3) NOT NULL,
  uom VARCHAR(20) NULL,
  PRIMARY KEY (company_id, tender_item_id),
  CONSTRAINT fk_ti_tender FOREIGN KEY (company_id, tender_id) REFERENCES tender(company_id, tender_id) ON DELETE CASCADE,
  CONSTRAINT fk_ti_inv FOREIGN KEY (company_id, inventory_id) REFERENCES inventory(company_id, inventory_id)
);

-- 투찰
CREATE TABLE IF NOT EXISTS bid (
  company_id CHAR(5) NOT NULL,
  bid_id CHAR(10) NOT NULL, -- 선두 4
  tender_id CHAR(10) NOT NULL,
  supplier_id CHAR(10) NOT NULL,
  bid_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total_amount DECIMAL(18,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED', -- SUBMITTED, AWARDED, LOST
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  PRIMARY KEY (company_id, bid_id),
  UNIQUE KEY uk_bid_tender_supplier (company_id, tender_id, supplier_id),
  CONSTRAINT fk_bid_tender FOREIGN KEY (company_id, tender_id) REFERENCES tender(company_id, tender_id),
  CONSTRAINT fk_bid_supplier FOREIGN KEY (company_id, supplier_id) REFERENCES supplier(company_id, supplier_id)
);

CREATE TABLE IF NOT EXISTS bid_item (
    company_id CHAR(5) NOT NULL,
    bid_item_id BIGINT AUTO_INCREMENT NOT NULL,
    bid_id CHAR(10) NOT NULL,
    tender_item_id BIGINT NOT NULL,
    unit_price DECIMAL(18,2) NOT NULL,
    PRIMARY KEY (company_id, bid_item_id),
    CONSTRAINT fk_bi_bid FOREIGN KEY (company_id, bid_id) REFERENCES bid(company_id, bid_id) ON DELETE CASCADE
);

-- 구매오더
CREATE TABLE IF NOT EXISTS purchase_order (
  company_id CHAR(5) NOT NULL,
  po_id CHAR(10) NOT NULL, -- 선두 4
  tender_id CHAR(10) NULL, -- 입찰 기반
  pr_id CHAR(10) NULL, -- 구매요청 기반
  supplier_id CHAR(10) NOT NULL,
  order_date DATE NOT NULL,
  delivery_date DATE NULL,
  total_amount DECIMAL(18,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ORDERED', -- ORDERED, PARTIALLY_RECEIVED, FULLY_RECEIVED, CANCELED
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, po_id),
  CONSTRAINT fk_po_tender FOREIGN KEY (company_id, tender_id) REFERENCES tender(company_id, tender_id),
  CONSTRAINT fk_po_pr FOREIGN KEY (company_id, pr_id) REFERENCES purchase_request(company_id, pr_id),
  CONSTRAINT fk_po_supplier FOREIGN KEY (company_id, supplier_id) REFERENCES supplier(company_id, supplier_id)
);

CREATE TABLE IF NOT EXISTS purchase_order_item (
  company_id CHAR(5) NOT NULL,
  po_item_id BIGINT AUTO_INCREMENT NOT NULL,
  po_id CHAR(10) NOT NULL,
  inventory_id CHAR(10) NOT NULL,
  item_name VARCHAR(100) NOT NULL,
  quantity DECIMAL(18,3) NOT NULL,
  unit_price DECIMAL(18,2) NOT NULL,
  item_total DECIMAL(18,2) AS (quantity * unit_price) STORED,
  uom VARCHAR(20) NULL,
  PRIMARY KEY (company_id, po_item_id),
  CONSTRAINT fk_poi_po FOREIGN KEY (company_id, po_id) REFERENCES purchase_order(company_id, po_id) ON DELETE CASCADE,
  CONSTRAINT fk_poi_inv FOREIGN KEY (company_id, inventory_id) REFERENCES inventory(company_id, inventory_id)
);

-- 입고
CREATE TABLE IF NOT EXISTS goods_receipt (
  company_id CHAR(5) NOT NULL,
  receipt_id CHAR(10) NOT NULL, -- 선두 4
  po_id CHAR(10) NOT NULL,
  supplier_id CHAR(10) NOT NULL,
  receipt_date DATE NOT NULL,
  receiver_user_id CHAR(5) NOT NULL,
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, receipt_id),
  CONSTRAINT fk_gr_po FOREIGN KEY (company_id, po_id) REFERENCES purchase_order(company_id, po_id),
  CONSTRAINT fk_gr_supplier FOREIGN KEY (company_id, supplier_id) REFERENCES supplier(company_id, supplier_id),
  CONSTRAINT fk_gr_receiver FOREIGN KEY (company_id, receiver_user_id) REFERENCES `user`(company_id, user_id)
);

CREATE TABLE IF NOT EXISTS goods_receipt_item (
  company_id CHAR(5) NOT NULL,
  receipt_item_id BIGINT AUTO_INCREMENT NOT NULL,
  receipt_id CHAR(10) NOT NULL,
  po_item_id BIGINT NOT NULL,
  inventory_id CHAR(10) NOT NULL,
  received_qty DECIMAL(18,3) NOT NULL,
  storage_id CHAR(5) NOT NULL, -- 입고 창고
  PRIMARY KEY (company_id, receipt_item_id),
  CONSTRAINT fk_gri_gr FOREIGN KEY (company_id, receipt_id) REFERENCES goods_receipt(company_id, receipt_id) ON DELETE CASCADE,
  CONSTRAINT fk_gri_inv FOREIGN KEY (company_id, inventory_id) REFERENCES inventory(company_id, inventory_id),
  CONSTRAINT fk_gri_storage FOREIGN KEY (company_id, storage_id) REFERENCES storage(company_id, storage_id)
);

-- 협력사 평가
CREATE TABLE IF NOT EXISTS supplier_evaluation (
  company_id CHAR(5) NOT NULL,
  eval_id CHAR(10) NOT NULL, -- 선두 4
  po_id CHAR(10) NOT NULL,
  supplier_id CHAR(10) NOT NULL,
  evaluator_user_id CHAR(5) NOT NULL,
  eval_date DATE NOT NULL,
  score_quality DECIMAL(3,1) NULL, -- 품질
  score_delivery DECIMAL(3,1) NULL, -- 납기
  score_price DECIMAL(3,1) NULL, -- 가격
  comment TEXT NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, eval_id),
  UNIQUE KEY uk_eval_po (company_id, po_id),
  CONSTRAINT fk_se_po FOREIGN KEY (company_id, po_id) REFERENCES purchase_order(company_id, po_id),
  CONSTRAINT fk_se_supplier FOREIGN KEY (company_id, supplier_id) REFERENCES supplier(company_id, supplier_id),
  CONSTRAINT fk_se_evaluator FOREIGN KEY (company_id, evaluator_user_id) REFERENCES `user`(company_id, user_id)
);
```

### 5.2. 초기 데이터 추가
시스템 운영에 필요한 초기 데이터를 아래와 같이 추가합니다.

```sql
-- 구매 관리용 ID 시퀀스 추가
INSERT INTO id_sequence (company_id, prefix, next_val) VALUES ('C0001','4',4000000000);

-- 재고 유형 코드 타입 추가
INSERT INTO code_type (company_id, code_type, code_type_name) VALUES ('C0001','INVTP','재고유형');

-- 재고 유형 코드 추가
INSERT INTO code (company_id, code_id, code_type, code_name, sort_order) VALUES
('C0001','CSM01','INVTP','소모품',10),
('C0001','PRT01','INVTP','부품',20),
('C0001','SVC01','INVTP','서비스',30);
```

### 5.3. 애플리케이션 아키텍처
- 신규 기능은 기존 `com.cmms` 패키지 하위에 모듈별로 패키지를 신설하여 구현합니다.
  - `com.cmms.purchase`: 구매 요청/오더/입고/평가 관련 로직
  - `com.cmms.supplier`: 협력사 정보 관리 로직
  - `com.cmms.tender`: 입찰/투찰 관련 로직
  - `com.cmms.portal`: 외부 협력사 포털 전용 컨트롤러 및 서비스
- 각 패키지는 기존 구조와 동일하게 `controller`, `service`, `repository`, `entity`, `dto`로 구성됩니다.
- 입고 처리(`GoodsReceiptService`) 시, 재고 서비스(`StockService`)를 호출하여 재고량 및 재고 이력을 원자적(Atomic)으로 업데이트합니다.

## 6. 신규 보고서
1.  **사이트/창고별 재고 현황 보고서:** 특정 시점의 사이트/창고별 재고 수량, 단가, 합계 금액을 표시합니다.
2.  **재고 입출고 이력 보고서:** 특정 기간 동안의 품목별 입고, 출고, 이동 이력을 상세히 추적할 수 있도록 표시합니다.
3.  **구매 이력 보고서:** 특정 기간 동안의 구매 오더 내역을 협력사별, 품목별로 집계하여 보여줍니다.
4.  **협력사 평가 보고서:** 협력사별 평가 점수(품질, 납기 등)를 종합하여 리포트합니다.
