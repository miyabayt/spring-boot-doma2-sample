package com.sample.web.base.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.servlet.support.csrf.CsrfRequestDataValueProcessor;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import lombok.val;

/**
 * 二重送信防止チェックのトークンを埋める
 */
public class DoubleSubmitCheckingRequestDataValueProcessor implements RequestDataValueProcessor {

    private static final CsrfRequestDataValueProcessor PROCESSOR = new CsrfRequestDataValueProcessor();

    private static final ThreadLocal<String> ACTION_HOLDER = new ThreadLocal<>();

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {
        ACTION_HOLDER.set(action);
        return PROCESSOR.processAction(request, action, httpMethod);
    }

    @Override
    public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {
        return PROCESSOR.processFormFieldValue(request, name, value, type);
    }

    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        val map = PROCESSOR.getExtraHiddenFields(request);

        if (!map.isEmpty()) {
            val action = ACTION_HOLDER.get();
            String token = DoubleSubmitCheckToken.getExpectedToken(request, action);

            if (token == null) {
                token = DoubleSubmitCheckToken.renewToken(request, action);
            }

            map.put(DoubleSubmitCheckToken.DOUBLE_SUBMIT_CHECK_PARAMETER, token);
            ACTION_HOLDER.remove();
        }

        return map;
    }

    @Override
    public String processUrl(HttpServletRequest request, String url) {
        return PROCESSOR.processUrl(request, url);
    }
}
