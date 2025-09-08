package com.cmms.common.exception;

/**
 * CMMS 비즈니스 예외
 * 코딩 룰 기반: 비즈니스 로직 예외 처리
 * 
 * @author cmms
 * @since 2024-03-19
 */
public class CmmsBusinessException extends RuntimeException {
    
    private final String errorCode;
    
    public CmmsBusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public CmmsBusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
