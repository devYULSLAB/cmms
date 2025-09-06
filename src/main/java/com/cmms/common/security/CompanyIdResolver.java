package com.cmms.common.security;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CompanyIdResolver implements HandlerMethodArgumentResolver {

    private static final String COMPANY_ID_HEADER = "X-Company-Id";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CompanyId.class) != null &&
               parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String companyId = webRequest.getHeader(COMPANY_ID_HEADER);
        if (companyId == null || companyId.isEmpty()) {
            throw new IllegalArgumentException("Missing or empty X-Company-Id header");
        }
        return companyId;
    }
}
