package com.cmms.common.logging.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CMMS 로깅 어노테이션
 * 코딩 룰 기반: MDC(companyId, username, traceId) 사용
 * 
 * @author cmms
 * @since 2024-03-19
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CmmsLoggable {
    
    /**
     * 로그 레벨 (기본값: INFO)
     */
    LogLevel level() default LogLevel.INFO;
    
    /**
     * 메서드 인자 포함 여부 (기본값: true)
     */
    boolean includeArgs() default true;
    
    /**
     * 메서드 결과 포함 여부 (기본값: false)
     */
    boolean includeResult() default false;
    
    /**
     * 실행 시간 측정 여부 (기본값: true)
     */
    boolean measureTime() default true;
    
    /**
     * 민감정보 필터링 여부 (기본값: true)
     */
    boolean filterSensitiveData() default true;
    
    /**
     * 로그 레벨 열거형
     */
    enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
