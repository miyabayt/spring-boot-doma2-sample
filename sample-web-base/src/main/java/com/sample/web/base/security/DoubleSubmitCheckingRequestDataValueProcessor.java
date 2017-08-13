package com.sample.web.base.security;

import lombok.val;
import org.springframework.security.web.servlet.support.csrf.CsrfRequestDataValueProcessor;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 二重送信防止チェックのトークンを埋める
 */
public class DoubleSubmitCheckingRequestDataValueProcessor implements RequestDataValueProcessor {

    private static final CsrfRequestDataValueProcessor PROCESSOR = new CsrfRequestDataValueProcessor();

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {
        return PROCESSOR.processAction(request, action, httpMethod);
    }

    @Override
    public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {
        return PROCESSOR.processFormFieldValue(request, name, value, type);
    }

    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        val map = PROCESSOR.getExtraHiddenFields(request);
        String token = DoubleSubmitCheckToken.getExpectedToken(request);
        if (token == null) {
            token = DoubleSubmitCheckToken.renewToken(request);
        }

        map.put(DoubleSubmitCheckToken.DOUBLE_SUBMIT_CHECK_PARAMETER, token);
        return map;
    }

    @Override
    public String processUrl(HttpServletRequest request, String url) {
        return PROCESSOR.processUrl(request, url);
    }
}
