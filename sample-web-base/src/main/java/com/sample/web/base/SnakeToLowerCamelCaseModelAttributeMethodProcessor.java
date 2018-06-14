package com.sample.web.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import lombok.val;

public class SnakeToLowerCamelCaseModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

    @Autowired
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * コンストラクタ
     * 
     * @param annotationNotRequired
     */
    public SnakeToLowerCamelCaseModelAttributeMethodProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest) {
        val target = binder.getTarget();
        val dataBinder = new SnakeToLowerCamelCaseRequestDataBinder(target, binder.getObjectName());
        requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(dataBinder, nativeWebRequest);
        super.bindRequestParameters(dataBinder, nativeWebRequest);
    }
}
