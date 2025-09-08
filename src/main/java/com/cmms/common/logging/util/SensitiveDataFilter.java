package com.cmms.common.logging.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 민감정보 필터링 유틸리티
 * 코딩 룰 기반: 민감정보 금지
 * 
 * @author cmms
 * @since 2024-03-19
 */
public class SensitiveDataFilter {
    
    // 민감정보 키워드 목록
    private static final List<String> SENSITIVE_KEYS = Arrays.asList(
        "password", "pwd", "pass", "secret", "token", "key", "auth",
        "credit", "card", "ssn", "social", "security", "number",
        "phone", "tel", "mobile", "email", "address", "zip"
    );
    
    // 패스워드 패턴
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "(?i)(password|pwd|pass)\\s*[:=]\\s*[^,\\s]+", 
        Pattern.CASE_INSENSITIVE
    );
    
    // 이메일 패턴
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"
    );
    
    // 전화번호 패턴
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "\\b(\\d{3}[-.]?\\d{3}[-.]?\\d{4}|\\(\\d{3}\\)\\s*\\d{3}[-.]?\\d{4})\\b"
    );
    
    /**
     * 민감정보 필터링
     */
    public static String filterSensitiveData(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        
        String filtered = data;
        
        // 패스워드 필터링
        filtered = PASSWORD_PATTERN.matcher(filtered).replaceAll("$1=***");
        
        // 이메일 필터링
        filtered = EMAIL_PATTERN.matcher(filtered).replaceAll("***@***.***");
        
        // 전화번호 필터링
        filtered = PHONE_PATTERN.matcher(filtered).replaceAll("***-***-****");
        
        // 키워드 기반 필터링
        for (String key : SENSITIVE_KEYS) {
            filtered = filterByKeyword(filtered, key);
        }
        
        return filtered;
    }
    
    /**
     * 키워드 기반 필터링
     */
    private static String filterByKeyword(String data, String keyword) {
        String pattern = "(?i)\\b" + keyword + "\\s*[:=]\\s*[^,\\s]+";
        return data.replaceAll(pattern, keyword + "=***");
    }
    
    /**
     * 객체의 민감정보 필터링
     */
    public static String filterSensitiveData(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        return filterSensitiveData(obj.toString());
    }
}
