/**
 * 프로그램명: CMMS 스키마 생성 및 초기 데이터 마이그레이션 (정리본)
 * 기능: CMMS 전용 스키마/테이블/인덱스/시퀀스/초기 코드 데이터 생성
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-09-04
 */

-- (옵션) 초기화
-- DROP SCHEMA IF EXISTS cmms;

CREATE SCHEMA IF NOT EXISTS cmms DEFAULT CHARACTER SET utf8mb4;
USE cmms;

-- =========================================================
-- 0) DOMAIN: 회사/사이트/부서/사용자/권한
-- =========================================================
CREATE TABLE IF NOT EXISTS company (
  company_id CHAR(5) NOT NULL,
  company_name VARCHAR(100) NOT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id)
);

CREATE TABLE IF NOT EXISTS site (
  company_id CHAR(5) NOT NULL,
  site_id CHAR(5) NOT NULL,
  site_name VARCHAR(100) NOT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, site_id),
  CONSTRAINT fk_site_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS dept (
  company_id CHAR(5) NOT NULL,
  dept_id CHAR(5) NOT NULL,
  dept_name VARCHAR(100) NOT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, dept_id),
  CONSTRAINT fk_dept_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS `user` (
  company_id CHAR(5) NOT NULL,
  user_id CHAR(5) NOT NULL,
  user_name VARCHAR(100) NOT NULL,
  password_hash        VARCHAR(100) NOT NULL,   -- BCrypt 해시만 저장
  password_updated_at  DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  failed_login_count   INT       NOT NULL DEFAULT 0,
  is_locked            CHAR(1)   NOT NULL DEFAULT 'N',
  last_login_at        DATETIME  NULL,
  must_change_pw       CHAR(1)   NOT NULL DEFAULT 'N',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, user_id),
  CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS role (
  company_id CHAR(5) NOT NULL,
  role_id CHAR(5) NOT NULL,
  role_name VARCHAR(100) NOT NULL,
  description VARCHAR(200) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, role_id),
  CONSTRAINT fk_role_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS user_role (
  company_id CHAR(5) NOT NULL,
  user_id  CHAR(5) NOT NULL,
  role_id  CHAR(5) NOT NULL,
  grant_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, user_id, role_id),
  CONSTRAINT fk_user_role_user FOREIGN KEY (company_id, user_id) REFERENCES `user`(company_id, user_id),
  CONSTRAINT fk_user_role_role FOREIGN KEY (company_id, role_id) REFERENCES role(company_id, role_id)
);

-- =========================================================
-- 1) 공통: ID 시퀀스 (prefix별 증가 관리)
-- =========================================================
CREATE TABLE IF NOT EXISTS id_sequence (
  company_id CHAR(5) NOT NULL,
  prefix CHAR(1) NOT NULL, -- 1:plant, 2:inventory, 3:inspection, 5:workorder, 7:workflow, 8:file/memo, 9:workpermit
  next_val BIGINT NOT NULL,
  PRIMARY KEY (company_id, prefix),
  CONSTRAINT fk_idseq_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- =========================================================
-- 2) 코드 타입/코드
-- =========================================================
CREATE TABLE IF NOT EXISTS code_type (
  company_id CHAR(5) NOT NULL,
  code_type VARCHAR(20) NOT NULL,   -- 예: JOBTP, ASSET, DEPRE, PERMT, LOC, FUNC
  code_type_name VARCHAR(100) NOT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  sort_order INT NOT NULL DEFAULT 0,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, code_type),
  CONSTRAINT fk_codetype_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS code (
  company_id CHAR(5) NOT NULL,
  code_id CHAR(5) NOT NULL,         -- 5자리 코드
  code_type VARCHAR(20) NOT NULL,   -- FK → code_type
  code_name VARCHAR(100) NOT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  sort_order INT NOT NULL DEFAULT 0,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, code_id),
  CONSTRAINT fk_code_type FOREIGN KEY (company_id, code_type)
    REFERENCES code_type(company_id, code_type)
);

-- =========================================================
-- 3) 기능/저장 위치
-- =========================================================
CREATE TABLE IF NOT EXISTS func (
  company_id CHAR(5) NOT NULL,
  func_id CHAR(5) NOT NULL,
  func_name VARCHAR(100) NOT NULL,
  parent_func_id CHAR(5) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  sort_order INT NOT NULL DEFAULT 0,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, func_id),
  CONSTRAINT fk_func_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS storage (
  company_id CHAR(5) NOT NULL,
  storage_id CHAR(5) NOT NULL,
  storage_name VARCHAR(100) NOT NULL,
  parent_storage_id CHAR(5) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  sort_order INT NOT NULL DEFAULT 0,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, storage_id),
  CONSTRAINT fk_storage_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- =========================================================
-- 4) 파일 그룹/첨부 (다른 테이블에서 참조는 안하지만 선생성)
-- =========================================================
CREATE TABLE IF NOT EXISTS file_group (
  company_id CHAR(5) NOT NULL,
  file_group_id CHAR(10) NOT NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, file_group_id),
  CONSTRAINT fk_filegroup_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS file_attach (
  company_id CHAR(5) NOT NULL,
  file_id CHAR(10) NOT NULL,  -- 선두 8
  file_group_id CHAR(10) NOT NULL,
  original_name VARCHAR(255) NOT NULL,
  stored_name VARCHAR(255) NOT NULL,
  mime_type VARCHAR(100) NULL,
  file_size BIGINT NULL,
  storage_path VARCHAR(500) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, file_id),
  KEY ix_attach_group (company_id, file_group_id),
  CONSTRAINT fk_attach_group FOREIGN KEY (company_id, file_group_id)
    REFERENCES file_group(company_id, file_group_id),
  CONSTRAINT fk_attach_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- =========================================================
-- 5) 설비/재고
-- =========================================================
CREATE TABLE IF NOT EXISTS plant (
  company_id CHAR(5) NOT NULL,
  
  -- 기본정보 
  plant_id CHAR(10) NOT NULL,                 -- 선두 1
  plant_name VARCHAR(100) NOT NULL,
  master_type CHAR(5) NULL,                   -- 코드(ASSET 등)
  site_id CHAR(5) NULL,
  func_id CHAR(5) NULL,
  dept_id CHAR(5) NULL,
  -- 제조사 정보 section
  maker_name VARCHAR(100) NULL,
  spec VARCHAR(100) NULL,
  model_no VARCHAR(100) NULL,
  serial_no VARCHAR(100) NULL,
  -- 재무 정보 section
  install_date DATE NULL,
  depre_type CHAR(5) NULL,                    -- 코드(DEPRE 등)
  depre_period INT NULL,
  acquire_cost DECIMAL(18,2) NULL,
  residual_value DECIMAL(18,2) NULL,
  -- 운영 정보 section
  preventive_yn   CHAR(1) NOT NULL DEFAULT 'N',
  psm_yn          CHAR(1) NOT NULL DEFAULT 'N',
  wp_target_yn    CHAR(1) NOT NULL DEFAULT 'N',
  -- 참고 정보
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, plant_id),
  CONSTRAINT fk_plant_site  FOREIGN KEY (company_id, site_id) REFERENCES site(company_id, site_id),
  CONSTRAINT fk_plant_func  FOREIGN KEY (company_id, func_id) REFERENCES func(company_id, func_id),
  CONSTRAINT fk_plant_dept  FOREIGN KEY (company_id, dept_id) REFERENCES dept(company_id, dept_id)
);

-- inventory는 회사 레벨, 재고는 site/storage 레벨
CREATE TABLE IF NOT EXISTS inventory (
  company_id CHAR(5) NOT NULL,
  -- 기본정보 
  inventory_id CHAR(10) NOT NULL,             -- 선두 2
  inventory_name VARCHAR(100) NOT NULL,
  master_type CHAR(5) NULL,
  dept_id CHAR(5) NULL,
  -- 제조사 정보 
  maker_name VARCHAR(100) NULL,
  spec VARCHAR(100) NULL,
  model_no VARCHAR(100) NULL,
  serial_no VARCHAR(100) NULL,
  -- 참고 정보 
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, inventory_id),
  CONSTRAINT fk_inv_company FOREIGN KEY (company_id) REFERENCES company(company_id),
  CONSTRAINT fk_inv_dept FOREIGN KEY (company_id, dept_id) REFERENCES dept(company_id, dept_id)
);

CREATE TABLE IF NOT EXISTS stock (
  company_id     CHAR(5)       NOT NULL,
  site_id        CHAR(5)       NOT NULL,
  storage_id     CHAR(5)       NOT NULL,      -- STOR
  inventory_id   CHAR(10)      NOT NULL,      -- inventory_master FK
  qty            DECIMAL(18,3) NOT NULL DEFAULT 0,
  unit_price     DECIMAL(18,2) NULL,
  total_amount   DECIMAL(18,2) AS (ROUND(qty * IFNULL(unit_price,0), 2)) STORED, -- 자동계산
  uom            VARCHAR(20)   NULL,          -- unit of measure
  updated_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, site_id, storage_id, inventory_id),
  CONSTRAINT fk_stock_site    FOREIGN KEY (company_id, site_id) REFERENCES site(company_id, site_id),
  CONSTRAINT fk_stock_inv     FOREIGN KEY (company_id, inventory_id) REFERENCES inventory(company_id, inventory_id),
  CONSTRAINT fk_stock_storage FOREIGN KEY (company_id, storage_id) REFERENCES storage(company_id, storage_id)
);

CREATE TABLE IF NOT EXISTS stock_tx (
  company_id         CHAR(5)       NOT NULL,
  tx_id              CHAR(10)      NOT NULL,     -- 선두 8 또는 별도 정책이면 조정
  site_id            CHAR(5)       NOT NULL,
  inventory_id       CHAR(10)      NOT NULL,
  action_type        VARCHAR(10)   NOT NULL,     -- IN/OUT/MOVE
  qty_change         DECIMAL(18,3) NOT NULL,
  unit_price         DECIMAL(18,2) NULL,
  amount_change      DECIMAL(18,2) NULL,
  src_storage_id     CHAR(5)       NULL,         -- STOR (애플리케이션 검증)
  dst_storage_id     CHAR(5)       NULL,         -- STOR
  before_qty         DECIMAL(18,3) NULL,
  after_qty          DECIMAL(18,3) NULL,
  related_type       VARCHAR(20)   NULL,         -- 'WO'/'INSP'/'WP' 등
  related_id         CHAR(10)      NULL,
  notes              VARCHAR(500)  NULL,
  created_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, tx_id),
  KEY idx_stock_tx_item (company_id, inventory_id),
  KEY idx_stock_tx_time (created_at),
  CONSTRAINT fk_stocktx_site FOREIGN KEY (company_id, site_id) REFERENCES site(company_id, site_id),
  CONSTRAINT fk_stocktx_inv  FOREIGN KEY (company_id, inventory_id) REFERENCES inventory(company_id, inventory_id)
  -- storage_id는 애플리케이션 검증
);

-- =========================================================
-- 6) 점검(Inspection)
-- =========================================================
CREATE TABLE IF NOT EXISTS inspection (
  company_id    CHAR(5)  NOT NULL,
  -- 기본정보 
  inspection_id CHAR(10) NOT NULL,             -- 선두 3 (번호정책 적용)
  inspection_name VARCHAR(100) NOT NULL,
  plant_id      CHAR(10) NOT NULL,
  job_type      CHAR(5)  NULL,                  -- 코드(JOBTP, 5자리)  
  site_id       CHAR(5)  NOT NULL,
  dept_id       CHAR(5)  NULL,
  -- 스케줄 정보 
  planned_date  DATETIME NULL,
  actual_date   DATETIME NULL,
  status        CHAR(1)  NOT NULL DEFAULT 'T',  -- T:임시, C:확정
  -- 참고 정보
  notes         VARCHAR(500),
  file_group_id CHAR(10),
  create_date   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, inspection_id),
  CONSTRAINT fk_insp_site  FOREIGN KEY (company_id, site_id) REFERENCES site(company_id, site_id),
  CONSTRAINT fk_insp_plant FOREIGN KEY (company_id, plant_id) REFERENCES plant(company_id, plant_id),
  CONSTRAINT fk_insp_dept  FOREIGN KEY (company_id, dept_id) REFERENCES dept(company_id, dept_id)
);

CREATE TABLE IF NOT EXISTS inspection_item (
  company_id    CHAR(5)  NOT NULL,
  inspection_id CHAR(10) NOT NULL,
  item_id       CHAR(2)  NOT NULL,             -- 2자리 아이템ID
  item_name     VARCHAR(100) NOT NULL,
  method        VARCHAR(100),
  unit          VARCHAR(20),
  min_val       DECIMAL(18,4),
  max_val       DECIMAL(18,4),
  std_val       DECIMAL(18,4),
  result_val    DECIMAL(18,4),
  notes         VARCHAR(500),
  create_date   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, inspection_id, item_id),
  CONSTRAINT fk_ii_inspection
    FOREIGN KEY (company_id, inspection_id)
    REFERENCES inspection(company_id, inspection_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- =========================================================
-- 7) 작업지시(WorkOrder)
-- =========================================================
CREATE TABLE IF NOT EXISTS workorder (
  company_id     CHAR(5)   NOT NULL,

  -- 기본정보
  workorder_id   CHAR(10)  NOT NULL,            -- 선두 5
  workorder_name VARCHAR(100) NOT NULL,
  plant_id       CHAR(10)  NULL,
  job_type       CHAR(5)   NULL,
  site_id        CHAR(5)   NULL,
  -- 스케줄 정보 
  planned_date   DATE      NULL,
  actual_date    DATE      NULL,
  status         CHAR(1)   NOT NULL DEFAULT 'T', -- T:임시, C:확정
  -- 비용/시간 정보
  planned_cost   DECIMAL(18,2) NULL,
  actual_cost    DECIMAL(18,2) NULL,
  planned_time   DECIMAL(18,2) NULL,            -- 시간(예: 인시)
  actual_time    DECIMAL(18,2) NULL,
  -- 참고 정보
  notes          VARCHAR(500) NULL,
  file_group_id  CHAR(10)  NULL,
  create_date    DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date    DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, workorder_id),
  CONSTRAINT fk_wo_site  FOREIGN KEY (company_id, site_id) REFERENCES site(company_id, site_id),
  CONSTRAINT fk_wo_plant FOREIGN KEY (company_id, plant_id) REFERENCES plant(company_id, plant_id)
);

CREATE TABLE IF NOT EXISTS workorder_item (
  company_id        CHAR(5)       NOT NULL,
  workorder_id      CHAR(10)      NOT NULL,
  item_id           CHAR(2)       NOT NULL,            -- 01~99
  item_name         VARCHAR(100)  NOT NULL,
  method            VARCHAR(100)  NULL,
  result_value      VARCHAR(100)  NULL,                -- 수치/텍스트 혼용 시 VARCHAR 유지
  part_inventory_id CHAR(10)      NULL,                -- 사용 부품(선택)
  qty               DECIMAL(18,3) NULL,
  uom               VARCHAR(20)   NULL,
  create_date       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, workorder_id, item_id),
  CONSTRAINT fk_woi_workorder
    FOREIGN KEY (company_id, workorder_id)
    REFERENCES workorder(company_id, workorder_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_woi_part_inv
    FOREIGN KEY (company_id, part_inventory_id)
    REFERENCES inventory(company_id, inventory_id)
);

-- =========================================================
-- 8) 작업허가(WorkPermit)
--   * 요구사항: permit_id는 회사 단위 유일 → PK에서 site_id 제외
-- =========================================================
CREATE TABLE IF NOT EXISTS workpermit (
  company_id   CHAR(5)      NOT NULL,

  -- 기본정보 
  permit_id    CHAR(10)     NOT NULL,              -- 선두 9 (회사 단위 유일)
  permit_name  VARCHAR(100) NOT NULL,
  permit_type  CHAR(5)      NULL,                  -- 코드(PERMT)
  workorder_id CHAR(10)     NULL,
  plant_id     CHAR(10)     NULL,
  site_id      CHAR(5)      NULL,
  dept_id      CHAR(5)      NULL,
  -- 스케줄 정보 
  start_date   DATE         NULL,
  end_date     DATE         NULL,
  status       CHAR(1)      NOT NULL DEFAULT 'T',  -- T:임시, C:확정
  -- 안전정보   
  work_summary  VARCHAR(100) NULL,
  hazard_factor VARCHAR(100) NULL,
  safety_factor VARCHAR(100) NULL,
  -- 참고 정보
  notes         VARCHAR(500)  NULL,
  file_group_id CHAR(10)      NULL,
  create_date   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (company_id, permit_id),
  CONSTRAINT fk_wp_company FOREIGN KEY (company_id) REFERENCES company(company_id),
  CONSTRAINT fk_wp_site    FOREIGN KEY (company_id, site_id) REFERENCES site(company_id, site_id),
  CONSTRAINT fk_wp_workorder FOREIGN KEY (company_id, workorder_id)
    REFERENCES workorder(company_id, workorder_id),
  CONSTRAINT fk_wp_plant FOREIGN KEY (company_id, plant_id)
    REFERENCES plant(company_id, plant_id),
  CONSTRAINT fk_wp_dept FOREIGN KEY (company_id, dept_id)
    REFERENCES dept(company_id, dept_id)
);

CREATE TABLE IF NOT EXISTS workpermit_item (
  company_id  CHAR(5)       NOT NULL,
  permit_id   CHAR(10)      NOT NULL,
  item_id     CHAR(2)       NOT NULL,             -- 01~99
  item_name   VARCHAR(100)  NOT NULL,
  signature   VARCHAR(100)  NULL,                 -- 서명(전자서명 식별자/파일ID 등)
  name        VARCHAR(100)  NULL,                 -- 서명자명
  create_date DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (company_id, permit_id, item_id),

  CONSTRAINT fk_wpi_workpermit
    FOREIGN KEY (company_id, permit_id)
    REFERENCES workpermit(company_id, permit_id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- =========================================================
-- 9) 메모(게시판)
-- =========================================================
CREATE TABLE IF NOT EXISTS memo (
  company_id CHAR(5) NOT NULL,
  memo_id CHAR(10) NOT NULL,  -- 선두 8
  memo_name VARCHAR(100) NOT NULL,
  isPinned CHAR(1) NOT NULL DEFAULT 'N',
  view_cnt INT NOT NULL DEFAULT 0,
  content MEDIUMTEXT NOT NULL,
  author_id CHAR(5) NOT NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, memo_id),
  CONSTRAINT fk_memo_company FOREIGN KEY (company_id) REFERENCES company(company_id),
  CONSTRAINT fk_memo_author  FOREIGN KEY (company_id, author_id) REFERENCES `user`(company_id, user_id)
);

CREATE TABLE IF NOT EXISTS memo_comment (
  company_id CHAR(5) NOT NULL,
  memo_id CHAR(10) NOT NULL,
  comment_id CHAR(10) NOT NULL,          -- (오타 수정) 10자리 사용 권장
  content VARCHAR(1000) NOT NULL,
  author_id CHAR(5) NOT NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, memo_id, comment_id),
  CONSTRAINT fk_mc_memo FOREIGN KEY (company_id, memo_id)
    REFERENCES memo(company_id, memo_id),
  CONSTRAINT fk_mc_author FOREIGN KEY (company_id, author_id)
    REFERENCES `user`(company_id, user_id)
);

-- =========================================================
-- 10) CMMS/WORKFLOW(결재)
-- =========================================================
CREATE TABLE IF NOT EXISTS approval_template (
  company_id CHAR(5) NOT NULL,
  template_id CHAR(10) NOT NULL,     -- 선두 7
  template_name VARCHAR(100) NOT NULL,
  description VARCHAR(300) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, template_id),
  CONSTRAINT fk_apptpl_company FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE IF NOT EXISTS approval_template_step (
  company_id CHAR(5) NOT NULL,
  template_id CHAR(10) NOT NULL,
  step_no INT NOT NULL,
  role_id CHAR(5) NULL,              -- 역할 기반 결재자
  approver_user CHAR(5) NULL,        -- 특정 사용자 지정
  condition_expr VARCHAR(200) NULL,  -- (선택) 조건식
  sort_order INT NOT NULL DEFAULT 0,
  PRIMARY KEY (company_id, template_id, step_no),
  CONSTRAINT fk_apptpl_step_tpl FOREIGN KEY (company_id, template_id)
    REFERENCES approval_template(company_id, template_id),
  CONSTRAINT fk_apptpl_step_role FOREIGN KEY (company_id, role_id)
    REFERENCES role(company_id, role_id),
  CONSTRAINT fk_apptpl_step_user FOREIGN KEY (company_id, approver_user)
    REFERENCES `user`(company_id, user_id)
);

CREATE TABLE IF NOT EXISTS approval_request (
  company_id CHAR(5) NOT NULL,
  approval_id CHAR(10) NOT NULL,     -- 선두 7
  template_id CHAR(10) NULL,
  title VARCHAR(150) NOT NULL,
  requester_user CHAR(5) NOT NULL,
  status CHAR(1) NOT NULL DEFAULT 'D',  -- D:임시,S:상신,P:진행,A:승인,R:반려
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, approval_id),
  CONSTRAINT fk_appreq_tpl FOREIGN KEY (company_id, template_id)
    REFERENCES approval_template(company_id, template_id),
  CONSTRAINT fk_appreq_user FOREIGN KEY (company_id, requester_user)
    REFERENCES `user`(company_id, user_id)
);

CREATE TABLE IF NOT EXISTS approval_request_step (
  company_id CHAR(5) NOT NULL,
  approval_id CHAR(10) NOT NULL,
  step_no INT NOT NULL,
  approver_user CHAR(5) NOT NULL,
  decision CHAR(1) NOT NULL DEFAULT 'W', -- W:대기,A:승인,R:반려
  decided_at DATETIME NULL,
  comment VARCHAR(500) NULL,
  sort_order INT NOT NULL DEFAULT 0,
  PRIMARY KEY (company_id, approval_id, step_no),
  CONSTRAINT fk_appreqstep_req FOREIGN KEY (company_id, approval_id)
    REFERENCES approval_request(company_id, approval_id),
  CONSTRAINT fk_appreqstep_user FOREIGN KEY (company_id, approver_user)
    REFERENCES `user`(company_id, user_id)
);

-- =========================================================
-- 11) 인덱스 (조회 다발 컬럼 위주)
-- =========================================================
CREATE INDEX ix_code_type_use ON code_type (company_id, use_yn, sort_order);
CREATE INDEX ix_code_type ON code (company_id, code_type, use_yn, sort_order);

CREATE INDEX ix_inventory_name ON inventory (company_id, inventory_name);
CREATE INDEX ix_plant_name ON plant (company_id, plant_name);

CREATE INDEX ix_stock_item ON stock (company_id, inventory_id);
CREATE INDEX ix_stocktx_rel ON stock_tx (company_id, related_type, related_id);

CREATE INDEX ix_inspection_dates ON inspection (company_id, planned_date, actual_date);
CREATE INDEX ix_workorder_dates ON workorder (company_id, planned_date, actual_date);

-- =========================================================
-- 12) 초기 데이터 (회사 C0001 가정)
-- =========================================================
INSERT INTO company (company_id, company_name) VALUES
('C0001','예시 회사');

INSERT INTO site (company_id, site_id, site_name) VALUES
('C0001','S0001','예시 사이트1'),
('C0001','S0002','예시 사이트2');

INSERT INTO dept (company_id, dept_id, dept_name) VALUES
('C0001','D0001','예시 부서'),
('C0001','D0002','예시 부서2'),
('C0001','D0003','예시 부서3');

INSERT INTO `user` (company_id, user_id, user_name, password_hash) VALUES
('C0001','ADMIN','관리자','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa'),
('C0001','U0001','예시 사용자1','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa'),
('C0001','U0002','예시 사용자2','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa'),
('C0001','U0003','예시 사용자3','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa');

INSERT INTO role (company_id, role_id, role_name) VALUES
('C0001','R0001','예시 권한'),
('C0001','R0002','예시 권한2'),
('C0001','R0003','예시 권한3'),
('C0001','APRV1','결재 1단계'),
('C0001','APRV2','결재 2단계');

INSERT INTO user_role (company_id, user_id, role_id) VALUES
('C0001','ADMIN','R0001'),
('C0001','U0001','R0001'),
('C0001','U0002','R0002'),
('C0001','U0003','R0003');

INSERT INTO id_sequence (company_id, prefix, next_val) VALUES
('C0001','1',1000000000),
('C0001','2',2000000000),
('C0001','3',3000000000),
('C0001','5',5000000000),
('C0001','7',7000000000),
('C0001','8',8000000000),
('C0001','9',9000000000);

INSERT INTO code_type (company_id, code_type, code_type_name) VALUES
('C0001','JOBTP','작업유형'),
('C0001','ASSET','자산유형'),
('C0001','DEPRE','감가유형'),
('C0001','PERMT','허가유형'),
('C0001','LOC','저장위치'),
('C0001','FUNC','기능위치');

INSERT INTO code (company_id, code_id, code_type, code_name, sort_order) VALUES
('C0001','PM001','JOBTP','예방정비',10),
('C0001','CM001','JOBTP','돌발정비',20),
('C0001','EQ001','ASSET','설비',10),
('C0001','SP001','ASSET','예비품',20),
('C0001','SL001','DEPRE','정액법',10),
('C0001','SLD01','DEPRE','정률법',20),
('C0001','WP001','PERMT','화기작업',10),
('C0001','WP002','PERMT','밀폐공간',20),
('C0001','L0101','LOC','창고1',10),
('C0001','L0102','LOC','창고2',20),
('C0001','F0001','FUNC','보일러동',10),
('C0001','F0002','FUNC','터빈동',20);

INSERT INTO func (company_id, func_id, func_name, sort_order) VALUES
('C0001','F0001','보일러동',10),
('C0001','F0002','터빈동',20);

INSERT INTO storage (company_id, storage_id, storage_name, sort_order) VALUES
('C0001','S101','창고1',10),
('C0001','S102','창고2',20);

-- WORKFLOW seed
INSERT INTO approval_template (company_id, template_id, template_name) VALUES
('C0001','7000000001','기본 결재 템플릿');

INSERT INTO approval_template_step (company_id, template_id, step_no, role_id, sort_order) VALUES
('C0001','7000000001',1,'APRV1',10),
('C0001','7000000001',2,'APRV2',20);
