/**
 * 프로그램명: CMMS 스키마 생성 및 초기 데이터 마이그레이션
 * 기능: CMMS 전용 스키마/테이블/인덱스/시퀀스/초기 코드 데이터 생성
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-09-04
 */

-- 0) 스키마
-- DROP SCHEMA IF EXISTS cmms;
CREATE SCHEMA cmms DEFAULT CHARACTER SET utf8mb4;
USE cmms;

-- 1) 공통: ID 시퀀스 (prefix별 증가 관리)
CREATE TABLE IF NOT EXISTS id_sequence (
  company_id CHAR(5) NOT NULL,
  prefix CHAR(1) NOT NULL, -- 1:plant, 2:inventory, 3:inspection, 5:workorder, 7:workflow, 9:workpermit
  next_val BIGINT NOT NULL,
  PRIMARY KEY (company_id, prefix)
);

-- 2) 코드 타입/코드
CREATE TABLE IF NOT EXISTS code_type (
  company_id CHAR(5) NOT NULL,
  code_type VARCHAR(20) NOT NULL,   -- 예: JOBTP, ASSET, DEPRE, PERMT, LOC, FUNC
  code_type_name VARCHAR(100) NOT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  sort_order INT NOT NULL DEFAULT 0,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, code_type)
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

-- 3) 기능/저장 위치 마스터
CREATE TABLE IF NOT EXISTS func_master (
  company_id CHAR(5) NOT NULL,
  func_id CHAR(5) NOT NULL,
  func_name VARCHAR(100) NOT NULL,
  parent_func_id CHAR(5) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  sort_order INT NOT NULL DEFAULT 0,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, func_id)
);

CREATE TABLE IF NOT EXISTS storage_master (
  company_id CHAR(5) NOT NULL,
  storage_id CHAR(5) NOT NULL,
  storage_name VARCHAR(100) NOT NULL,
  parent_storage_id CHAR(5) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  sort_order INT NOT NULL DEFAULT 0,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, storage_id)
);

-- 4) 설비/재고 마스터
CREATE TABLE IF NOT EXISTS plant_master (
  company_id CHAR(5) NOT NULL,
  site_id CHAR(5) NOT NULL,
  -- 기본정보 
  plant_id CHAR(10) NOT NULL,                         -- 선두 1
  plant_name VARCHAR(100) NOT NULL,
  master_type CHAR(5) NULL,                           -- 코드(ASSET 등)
  func_id CHAR(5) NULL,
  dept_id CHAR(5) NULL,
  -- 제조사 정보 section
  maker_name VARCHAR(100) NULL,
  spec VARCHAR(100) NULL,
  model_no VARCHAR(100) NULL,
  serial_no VARCHAR(100) NULL,
  -- 재무 정보 section
  install_date DATE NULL,                            
  depre_type CHAR(5) NULL,                           -- 코드(DEPRE 등)
  depre_period INT NULL,
  acquire_cost DECIMAL(18,2) NULL,
  residual_value DECIMAL(18,2) NULL,
  -- 운영 정보 section
  preventive_yn   CHAR(1)      NOT NULL DEFAULT 'N',
  psm_yn          CHAR(1)      NOT NULL DEFAULT 'N',
  wp_target_yn    CHAR(1)      NOT NULL DEFAULT 'N',  
  -- 참고 정보
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, site_id, plant_id)
);


-- plant mastger와 달리 inventory master 는 company level로 생성하고, stock는 site/storage level로 생성
CREATE TABLE IF NOT EXISTS inventory_master (
  company_id CHAR(5) NOT NULL,
  -- 기본정보 
  inventory_id CHAR(10) NOT NULL,                     -- 선두 2
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
  PRIMARY KEY (company_id, inventory_id)
);

CREATE TABLE IF NOT EXISTS stock (
  company_id     CHAR(5)       NOT NULL,
  site_id        CHAR(5)       NOT NULL,
  storage_id     CHAR(5)       NOT NULL,         -- STOR
  inventory_id   CHAR(10)      NOT NULL,         -- inventory_master FK
  qty            DECIMAL(18,3) NOT NULL DEFAULT 0,
  unit_price     DECIMAL(18,2) NULL,
  total_amount   DECIMAL(18,2) NULL,             -- qty * unit_price
  uom            VARCHAR(20)   NULL,             -- unit of measure
  updated_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, site_id, storage_id, inventory_id),
  FOREIGN KEY (company_id, site_id) REFERENCES site(company_id, site_id),
  FOREIGN KEY (company_id, inventory_id) REFERENCES inventory_master(company_id, inventory_id)
);

CREATE TABLE IF NOT EXISTS stock_tx (
  company_id        CHAR(5)       NOT NULL,
  tx_id             CHAR(10)      NOT NULL,        -- AUTO: entity_code='STOCKTX'로 10자리 생성
  site_id           CHAR(5)       NOT NULL,
  inventory_id      CHAR(10)      NOT NULL,
  action_type       VARCHAR(10)   NOT NULL,        -- IN/OUT/MOVE
  qty_change        DECIMAL(18,3) NOT NULL,
  unit_price        DECIMAL(18,2) NULL,
  amount_change     DECIMAL(18,2) NULL,
  src_storage_id    CHAR(5)       NULL,            -- STOR
  dst_storage_id    CHAR(5)       NULL,            -- STOR
  before_qty        DECIMAL(18,3) NULL,
  after_qty         DECIMAL(18,3) NULL,
  related_type      VARCHAR(20)   NULL,            -- 'WO'/'INSP'/'WP' 등
  related_id        CHAR(10)      NULL,
  notes             VARCHAR(500)  NULL,
  created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, tx_id),
  KEY idx_stock_tx_item (company_id, inventory_id),
  KEY idx_stock_tx_time (created_at),
  FOREIGN KEY (company_id, site_id) REFERENCES site(company_id, site_id),
  FOREIGN KEY (company_id, inventory_id) REFERENCES inventory_master(company_id, inventory_id)
  -- storage_id는 STOR 코드 애플리케이션 검증
);

-- 5) 점검(Inspection)
CREATE TABLE IF NOT EXISTS inspection (
  company_id CHAR(5) NOT NULL,
  site_id CHAR(5) NOT NULL,
  -- 기본정보 
  inspection_id CHAR(10) NOT NULL,                    -- 선두 3
  inspection_name VARCHAR(100) NOT NULL,
  plant_id CHAR(10) NULL,  
  job_type CHAR(5) NULL,                              -- 코드(JOBTP)
  dept_idCHAR(5) NULL,
  -- 스케줄 정보 
  planned_date DATE NULL,
  actual_date DATE NULL,
  status CHAR(1) NOT NULL DEFAULT 'T',                -- T:임시, C:확정
  -- 참고 정보
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, site_id, inspection_id)
);


CREATE TABLE IF NOT EXISTS inspection_item (
  company_id CHAR(5) NOT NULL,
  inspection_id CHAR(10) NOT NULL,
  item_id CHAR(2) NOT NULL,                           -- 2자리 아이템ID
  item_name VARCHAR(100) NOT NULL,
  method VARCHAR(100) NULL,
  unit VARCHAR(20) NULL,
  min_val DECIMAL(18,4) NULL,
  max_val DECIMAL(18,4) NULL,
  std_val DECIMAL(18,4) NULL,
  result_val DECIMAL(18,4) NULL,
  notes VARCHAR(500) NULL,
  PRIMARY KEY (company_id, inspection_id, item_id),
  CONSTRAINT fk_ii_inspection FOREIGN KEY (company_id, inspection_id)
    REFERENCES inspection(company_id, inspection_id)
);

-- 6) 작업지시(WorkOrder)
CREATE TABLE IF NOT EXISTS workorder (
  company_id CHAR(5) NOT NULL,
  site_id CHAR(5) NOT NULL,
  -- 기본정보 
  workorder_id CHAR(10) NOT NULL,                     -- 선두 5
  workorder_name VARCHAR(100) NOT NULL,
  plant_id CHAR(10) NULL,
  job_type CHAR(5) NULL,
  -- 스케줄 정보 
  planned_date DATE NULL,
  actual_date DATE NULL,
  status CHAR(1) NOT NULL DEFAULT 'T',                -- T:임시, C:확정
  -- 비용 정보
  planned_cost DECIMAL(18,2) NULL,
  actual_cost DECIMAL(18,2) NULL,
  planned_time DECIMAL(18,2) NULL,
  actual_time DECIMAL(18,2) NULL,
  -- 참고 정보
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, workorder_id)
);

CREATE TABLE IF NOT EXISTS workorder_item (
  company_id        CHAR(5)       NOT NULL,
  workorder_id     CHAR(10)      NOT NULL,
  item_id           CHAR(2)       NOT NULL,   -- 01~99
  item_name         VARCHAR(100)  NOT NULL,
  method            VARCHAR(100)  NULL,
  result_value      VARCHAR(100)  NULL,
  part_inventory_id CHAR(10)      NULL,       -- 사용 부품(선택)
  qty               DECIMAL(18,3) NULL,
  uom               VARCHAR(20)   NULL,
  create_date        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, workorder_id, item_id),
  FOREIGN KEY (company_id, workorder_id) REFERENCES workorder(company_id, workorder_id),
  FOREIGN KEY (company_id, part_inventory_id) REFERENCES inventory_master(company_id, inventory_id)
);

-- 7) 작업허가(WorkPermit)
CREATE TABLE IF NOT EXISTS workpermit (
  company_id CHAR(5) NOT NULL,
  site_id CHAR(5) NOT NULL,
  -- 기본정보 
  permit_id CHAR(10) NOT NULL,                        -- 선두 9
  permit_name VARCHAR(100) NOT NULL,
  permit_type CHAR(5) NULL,                           -- 코드(PERMT)
  workorder_id CHAR(10) NULL,
  plant_id CHAR(10) NULL,
  dept_id CHAR(5) NULL,
  -- 스케줄 정보 
  start_date DATE NULL,
  end_date DATE NULL,
  status CHAR(1) NOT NULL DEFAULT 'T',                -- T:임시, C:확정
  -- 안전정보   
  work_summary VARCHAR(100) NULL,
  hazard_factor VARCHAR(100) NULL,
  safety_factor VARCHAR(100) NULL,
  -- 참고 정보
  notes VARCHAR(500) NULL,
  file_group_id CHAR(10) NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, permit_id)
);

CREATE TABLE IF NOT EXISTS workpermit_item (
  company_id      CHAR(5)       NOT NULL,
  workpermit_id  CHAR(10)      NOT NULL,
  item_id         CHAR(2)       NOT NULL,   -- 01~99
  item_name       VARCHAR(100)  NOT NULL,
  signature       VARCHAR(100)  NULL,  -- 서명
  name            VARCHAR(100)  NULL,  -- 서명자 
  create_date      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, workpermit_id, item_id),
  FOREIGN KEY (company_id, workpermit_id) REFERENCES workpermit(company_id, workpermit_id)
);

-- 8) 메모(게시판)
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
  PRIMARY KEY (company_id, memo_id)
);

CREATE TABLE IF NOT EXISTS memo_comment (
  company_id CHAR(5) NOT NULL,
  memo_id CHAR(10) NOT NULL,
  comment_id CHAR(102) NOT NULL,
  content VARCHAR(1000) NOT NULL,
  author_id CHAR(5) NOT NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, memo_id, comment_id),
  CONSTRAINT fk_mc_memo FOREIGN KEY (company_id, memo_id)
    REFERENCES memo(company_id, memo_id)
);

-- 9) 파일 그룹/첨부/임시

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
    REFERENCES file_group(company_id, file_group_id)
);


-- 10) DOMAIN: 회사/사이트/부서/사용자/권한/사용자-권한
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
  PRIMARY KEY (company_id, site_id)
);

CREATE TABLE IF NOT EXISTS dept (
  company_id CHAR(5) NOT NULL,
  dept_id CHAR(5) NOT NULL,
  dept_name VARCHAR(100) NOT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, dept_id)
);

CREATE TABLE IF NOT EXISTS user (
  company_id CHAR(5) NOT NULL,
  user_id CHAR(5) NOT NULL,
  user_name VARCHAR(100) NOT NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, user_id)
);


CREATE TABLE role (
  company_id CHAR(5) NOT NULL,
  role_id CHAR(5) NOT NULL,
  role_name VARCHAR(100) NOT NULL,
  description VARCHAR(200) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, role_id)
);

CREATE TABLE user_role (
  company_id CHAR(5) NOT NULL,
  user_id  CHAR(5) NOT NULL,
  role_id   CHAR(5) NOT NULL,
  grant_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, user_id, role_id),
  CONSTRAINT fk_user_role_role FOREIGN KEY (company_id, role_id)
    REFERENCES role(company_id, role_id)
);

-- 11) CMMS/WORKFLOW(결재)
CREATE TABLE approval_template (
  company_id CHAR(5) NOT NULL,
  template_id CHAR(10) NOT NULL,     -- 선두 7
  template_name VARCHAR(100) NOT NULL,
  description VARCHAR(300) NULL,
  use_yn CHAR(1) NOT NULL DEFAULT 'Y',
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (company_id, template_id)
);

CREATE TABLE approval_template_step (
  company_id CHAR(5) NOT NULL,
  template_id CHAR(10) NOT NULL,
  step_no INT NOT NULL,
  role_id CHAR(5) NULL,              -- 역할 기반 결재자
  approver_user CHAR(5) NULL,        -- 특정 사용자 지정
  condition_expr VARCHAR(200) NULL,  -- (선택) 조건식
  sort_order INT NOT NULL DEFAULT 0,
  PRIMARY KEY (company_id, template_id, step_no),
  CONSTRAINT fk_apptpl_step_tpl FOREIGN KEY (company_id, template_id)
    REFERENCES approval_template(company_id, template_id)
);

CREATE TABLE approval_request (
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
    REFERENCES approval_template(company_id, template_id)
);

CREATE TABLE approval_request_step (
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
    REFERENCES approval_request(company_id, approval_id)
);

-- 12) 인덱스
CREATE INDEX ix_plant_func ON plant_master(company_id, func_id);
CREATE INDEX ix_inventory_storage ON inventory_master(company_id, storage_id);
CREATE INDEX ix_inspection_status ON inspection(company_id, status);
CREATE INDEX ix_workorder_date ON workorder(company_id, perform_date);

-- 13) 초기 데이터 (회사 C0001 가정)
INSERT INTO id_sequence (company_id, prefix, next_val) VALUES
('C0001','1',1000000000),
('C0001','2',2000000000),
('C0001','3',3000000000),
('C0001','5',5000000000),
('C0001','7',7000000000),
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

INSERT INTO func_master (company_id, func_id, func_name, sort_order) VALUES
('C0001','F0001','보일러동',10),
('C0001','F0002','터빈동',20);

INSERT INTO storage_master (company_id, storage_id, storage_name, sort_order) VALUES
('C0001','S101','창고1',10),
('C0001','S102','창고2',20);

-- 샘플 설비/재고
INSERT INTO plant_master (company_id, plant_id, plant_name, master_type, func_id, notes)
VALUES ('C0001','1000000001','순환수펌프','EQ001','F0001','샘플 설비');

INSERT INTO inventory_master (company_id, inventory_id, inventory_name, master_type, storage_id, notes)
VALUES ('C0001','2000000001','패킹세트','SP001','S101','샘플 자재');

-- DOMAIN seed
INSERT INTO role (company_id, role_id, role_name) VALUES
('C0001','APRV1','결재자1'),
('C0001','APRV2','결재자2');

-- WORKFLOW seed
INSERT INTO approval_template (company_id, template_id, template_name) VALUES
('C0001','7000000001','기본 결재 템플릿');

INSERT INTO approval_template_step (company_id, template_id, step_no, role_id, sort_order) VALUES
('C0001','7000000001',1,'APRV1',10),
('C0001','7000000001',2,'APRV2',20);
