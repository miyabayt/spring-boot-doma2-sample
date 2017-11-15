package com.sample.web.base.controller.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice(annotations = RestController.class) // HTMLコントローラーの例外を除外する
@Slf4j
public class JsonpControllerAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpControllerAdvice() {
        super("callback");
    }
}
