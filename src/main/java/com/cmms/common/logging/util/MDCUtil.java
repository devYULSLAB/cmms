package com.cmms.common.logging.util;

import org.slf4j.MDC;

/**
 * MDC 유틸리티 클래스
 * 코딩 룰 기반: companyId, username, traceId 관리
 * 
 * @author cmms
 * @since 2024-03-19
 */
public class MDCUtil {
    
    public static final String COMPANY_ID_KEY = "companyId";
    public static final String USERNAME_KEY = "username";
    public static final String TRACE_ID_KEY = "traceId";
    
    /**
     * MDC에 값 설정
     */
    public static void put(String key, String value) {
        if (value != null) {
            MDC.put(key, value);
        }
    }
    
    /**
     * MDC에서 값 조회
     */
    public static String get(String key) {
        return MDC.get(key);
    }
    
    /**
     * CompanyId 설정
     */
    public static void setCompanyId(String companyId) {
        put(COMPANY_ID_KEY, companyId);
    }
    
    /**
     * Username 설정
     */
    public static void setUsername(String username) {
        put(USERNAME_KEY, username);
    }
    
    /**
     * TraceId 설정
     */
    public static void setTraceId(String traceId) {
        put(TRACE_ID_KEY, traceId);
    }
    
    /**
     * CompanyId 조회
     */
    public static String getCompanyId() {
        return get(COMPANY_ID_KEY);
    }
    
    /**
     * Username 조회
     */
    public static String getUsername() {
        return get(USERNAME_KEY);
    }
    
    /**
     * TraceId 조회
     */
    public static String getTraceId() {
        return get(TRACE_ID_KEY);
    }
    
    /**
     * MDC 정리
     */
    public static void clear() {
        MDC.clear();
    }
    
    /**
     * 특정 키만 제거
     */
    public static void remove(String key) {
        MDC.remove(key);
    }
}
