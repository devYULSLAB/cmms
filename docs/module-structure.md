/**
 * 프로그램명: CMMS 모듈 구조 및 역할 안내
 * 기능: 각 모듈별 역할, 디렉터리/패키지/템플릿 구성 가이드
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-09-04
 */

# CMMS 구조 및 역할 안내

## 1. 모듈 개요
- **cmms**: plant, inventory, inspection(+item_id 2자리), workorder, workpermit, memo, **workflow(결재)**, **domain**, config(security config), auth(로그인 처리), common(공통 기능)
- common은 하위에 file(fileUploadFragment,fileListFragment),code(code type, code id 관리 )

## 2. 디렉토리 구조 
src/main/java/com/cmms/
  ├─ plant/{controller,service,repository,entity,dto}  
      ├─ func/    
  ├─ inventory/{controller,service,repository,entity,dto}
      ├─ storage/  
  ├─ inspection/{controller,service,repository,entity,dto}
  ├─ workorder/{controller,service,repository,entity,dto}
  ├─ workpermit/{controller,service,repository,entity,dto}
  ├─ memo/{controller,service,repository,entity,dto}
  ├─ workflow/{controller,service,repository,entity,dto}
  ├─ domain/    
      ├─ {company,site,dept,user,role,use_role}
  ├─ common/    
      ├─ {file, code, codeType}
src/main/resources/templates
  ├─ auth/Login.html
  ├─ layout/defaultLayout.hml
  ├─ {plant,inventory...}
src/main/resources/messages

## 3. application.yml
application.yml, -dev.yml, -prod.yml로 구분 

spring:
  # 데이터베이스 설정 (환경변수 또는 외부 설정으로 관리)
  datasource:
    url: ${DB_URL:jdbc:mariadb://localhost:3306/cmms}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:}
    driver-class-name: org.mariadb.jdbc.Driver
cmms:
  default-company-id: ${DEFAULT_COMPANY_ID:C0001}
# 파일 업로드 설정
servlet:
multipart:
    max-file-size: 50MB
    max-request-size: 100MB
# 파일 업로드 설정
file:
upload:
    root-path: uploads
    max-file-size: 50MB
    allowed-file-types: jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx,txt
    max-file-count: 10    
 
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

  ## 7. 로그인 처리 
  - company_Id 는 application.yml의 default-company-code : C0001 (또는 사용자 설정값) 
  - Login.html에서 companyId를 받지 말고 userId, password 만 받고 default-company-code를 주입하며, 추후 멀티 테넌트 형식으로 변경 가능하도록만 준비
  (예시)
    public interface CompanyIdResolver {
        String resolve(HttpServletRequest request);
        }

        @Component
        @RequiredArgsConstructor
        class DefaultCompanyIdResolver implements CompanyIdResolver {
            @Value("${app.default-company-code}")
            private final String defaultCompanyId;

            @Override
            public String resolve(HttpServletRequest request) {
                return defaultCompanyId; // 현재는 항상 C0001
            }
        }
--추후 서브도메인 방식으로 바꿀 때는 SubdomainCompanyIdResolver를 만들어 빈 교체만 하면 됩니다.
<form method="post" action="/login">
  <input type="text" name="userId" placeholder="User ID" required />
  <input type="password" name="password" placeholder="Password" required />
  <button type="submit">Login</button>
</form>
--
    @Component
    @RequiredArgsConstructor
    public class CompanyUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

        private final CompanyIdResolver companyIdResolver;

        public CompanyUsernamePasswordAuthenticationFilter(CompanyIdResolver resolver) {
            super(new AntPathRequestMatcher("/login", "POST"));
            this.companyIdResolver = resolver;
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
                throws AuthenticationException, IOException, ServletException {

            String userId   = req.getParameter("userId");
            String password = req.getParameter("password");
            if (userId == null || password == null) {
                throw new BadCredentialsException("Missing credentials");
            }

            String companyId = companyIdResolver.resolve(req); // ★ 현재는 C0001
            var authRequest  = new CompanyUsernamePasswordAuthenticationToken(companyId, userId.trim(), password);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }
