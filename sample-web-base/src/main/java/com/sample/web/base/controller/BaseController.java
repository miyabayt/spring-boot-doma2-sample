package com.sample.web.base.controller;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class BaseController {

    public static final String VALIDATION_ERROR = "ValidationError";

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected ModelMapper modelMapper;

    /**
     * 入力エラーの共通メッセージを返す
     * 
     * @return
     */
    public String getValidationErrorMessage() {
        return getMessage(VALIDATION_ERROR);
    }

    /**
     * メッセージを取得する
     * 
     * @param key
     * @param args
     * @return
     */
    public String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    /**
     * ロケールを指定してメッセージを取得する
     * 
     * @param key
     * @param args
     * @param locale
     * @return
     */
    public String getMessage(String key, Object[] args, Locale locale) {
        return messageSource.getMessage(key, args, locale);
    }
}
