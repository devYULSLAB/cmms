package com.cmms.common.exception;

import com.cmms.common.logging.util.MDCUtil;
import com.cmms.common.logging.util.SensitiveDataFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * CMMS 공통 예외 처리
 * 코딩 룰 기반: @ControllerAdvice 공통 예외 처리
 * 
 * @author cmms
 * @since 2024-03-19
 */
@ControllerAdvice
@Slf4j
public class CmmsExceptionHandler {

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(CmmsBusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(
            CmmsBusinessException e, HttpServletRequest request) {
        
        log.error("Business Exception: {} - {}", e.getErrorCode(), e.getMessage(), e);
        
        Map<String, Object> response = createErrorResponse(
            e.getErrorCode(), 
            e.getMessage(), 
            HttpStatus.BAD_REQUEST,
            request
        );
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 인증 예외 처리
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
            AuthenticationException e, HttpServletRequest request) {
        
        log.warn("Authentication Exception: {}", SensitiveDataFilter.filterSensitiveData(e.getMessage()));
        
        Map<String, Object> response = createErrorResponse(
            "AUTH_FAILED", 
            "인증에 실패했습니다.", 
            HttpStatus.UNAUTHORIZED,
            request
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 권한 예외 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {
        
        log.warn("Access Denied: {}", SensitiveDataFilter.filterSensitiveData(e.getMessage()));
        
        Map<String, Object> response = createErrorResponse(
            "ACCESS_DENIED", 
            "접근 권한이 없습니다.", 
            HttpStatus.FORBIDDEN,
            request
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 유효성 검증 예외 처리
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<Map<String, Object>> handleValidationException(
            Exception e, HttpServletRequest request) {
        
        log.warn("Validation Exception: {}", SensitiveDataFilter.filterSensitiveData(e.getMessage()));
        
        Map<String, Object> response = createErrorResponse(
            "VALIDATION_FAILED", 
            "입력 데이터가 유효하지 않습니다.", 
            HttpStatus.BAD_REQUEST,
            request
        );
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 일반 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception e, HttpServletRequest request) {
        
        log.error("Unexpected Exception: {}", SensitiveDataFilter.filterSensitiveData(e.getMessage()), e);
        
        Map<String, Object> response = createErrorResponse(
            "INTERNAL_ERROR", 
            "시스템 오류가 발생했습니다.", 
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 에러 응답 생성
     * 코딩 룰 기반: API 응답 포맷 {"success":false,"data":null,"message":"오류 메시지"}
     */
    private Map<String, Object> createErrorResponse(String errorCode, String message, HttpStatus status, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        // 코딩 룰 기반 응답 포맷
        response.put("success", false);
        response.put("data", null);
        response.put("message", message);
        response.put("errorCode", errorCode);
        response.put("timestamp", LocalDateTime.now());
        response.put("path", request.getRequestURI());
        
        // MDC 정보 추가
        response.put("companyId", MDCUtil.getCompanyId());
        response.put("username", MDCUtil.getUsername());
        response.put("traceId", MDCUtil.getTraceId());
        
        return response;
    }
}
