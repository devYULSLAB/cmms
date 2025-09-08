package com.cmms.common.logging.aspect;

import com.cmms.common.logging.annotation.CmmsLoggable;
import com.cmms.common.logging.util.MDCUtil;
import com.cmms.common.logging.util.SensitiveDataFilter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * CMMS 로깅 Aspect
 * 코딩 룰 기반: MDC(companyId, username, traceId) 사용
 * 
 * @author cmms
 * @since 2024-03-19
 */
@Aspect
@Component
@Slf4j
public class CmmsLoggingAspect {

    /**
     * @CmmsLoggable 어노테이션이 적용된 메서드에 대한 로깅
     */
    @Around("@annotation(cmmsLoggable)")
    public Object logMethod(ProceedingJoinPoint joinPoint, CmmsLoggable cmmsLoggable) throws Throwable {
        return executeWithLogging(joinPoint, cmmsLoggable);
    }

    /**
     * @CmmsLoggable 어노테이션이 적용된 클래스의 모든 메서드에 대한 로깅
     */
    @Around("@within(cmmsLoggable)")
    public Object logClass(ProceedingJoinPoint joinPoint, CmmsLoggable cmmsLoggable) throws Throwable {
        return executeWithLogging(joinPoint, cmmsLoggable);
    }

    /**
     * 실제 로깅 로직 실행
     */
    private Object executeWithLogging(ProceedingJoinPoint joinPoint, CmmsLoggable cmmsLoggable) throws Throwable {
        // MDC 설정
        setupMDC();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 메서드 시작 로깅
            logMethodStart(className, methodName, joinPoint.getArgs(), cmmsLoggable);
            
            // 메서드 실행
            Object result = joinPoint.proceed();
            
            // 메서드 완료 로깅
            long executionTime = System.currentTimeMillis() - startTime;
            logMethodEnd(className, methodName, result, executionTime, cmmsLoggable);
            
            return result;
            
        } catch (Exception e) {
            // 예외 발생 로깅
            long executionTime = System.currentTimeMillis() - startTime;
            logMethodError(className, methodName, e, executionTime, cmmsLoggable);
            throw e;
            
        } finally {
            // MDC 정리
            MDCUtil.clear();
        }
    }

    /**
     * MDC 설정
     */
    private void setupMDC() {
        // TraceId 생성 (요청별 고유 식별자)
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDCUtil.put("traceId", traceId);
        
        // CompanyId 설정
        String companyId = getCurrentCompanyId();
        MDCUtil.put("companyId", companyId);
        
        // Username 설정
        String username = getCurrentUsername();
        MDCUtil.put("username", username);
    }

    /**
     * 현재 사용자의 CompanyId 조회
     */
    private String getCurrentCompanyId() {
        try {
            // Security Context에서 조회
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                // CustomUserDetails에서 companyId 추출
                Object principal = auth.getPrincipal();
                if (principal.getClass().getSimpleName().equals("CustomUserDetails")) {
                    // 리플렉션을 통한 companyId 조회
                    try {
                        return (String) principal.getClass().getMethod("getCompanyId").invoke(principal);
                    } catch (Exception e) {
                        // 리플렉션 실패 시 기본값
                    }
                }
            }
            
            // Request에서 조회
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String companyId = request.getParameter("companyId");
                if (companyId != null) {
                    return companyId;
                }
            }
            
        } catch (Exception e) {
            log.debug("Failed to get companyId: {}", e.getMessage());
        }
        
        return "UNKNOWN";
    }

    /**
     * 현재 사용자의 Username 조회
     */
    private String getCurrentUsername() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                return auth.getName();
            }
        } catch (Exception e) {
            log.debug("Failed to get username: {}", e.getMessage());
        }
        
        return "ANONYMOUS";
    }

    /**
     * 메서드 시작 로깅
     */
    private void logMethodStart(String className, String methodName, Object[] args, CmmsLoggable cmmsLoggable) {
        if (cmmsLoggable.includeArgs()) {
            String filteredArgs = SensitiveDataFilter.filterSensitiveData(Arrays.toString(args));
            log.info("Starting {}.{} with args: {}", className, methodName, filteredArgs);
        } else {
            log.info("Starting {}.{}", className, methodName);
        }
    }

    /**
     * 메서드 완료 로깅
     */
    private void logMethodEnd(String className, String methodName, Object result, long executionTime, CmmsLoggable cmmsLoggable) {
        if (cmmsLoggable.includeResult()) {
            String filteredResult = SensitiveDataFilter.filterSensitiveData(result != null ? result.toString() : "null");
            log.info("Completed {}.{} in {}ms with result: {}", className, methodName, executionTime, filteredResult);
        } else if (cmmsLoggable.measureTime()) {
            log.info("Completed {}.{} in {}ms", className, methodName, executionTime);
        } else {
            log.info("Completed {}.{}", className, methodName);
        }
    }

    /**
     * 메서드 오류 로깅
     */
    private void logMethodError(String className, String methodName, Exception e, long executionTime, CmmsLoggable cmmsLoggable) {
        log.error("Error in {}.{} after {}ms: {}", className, methodName, executionTime, e.getMessage(), e);
    }
}
