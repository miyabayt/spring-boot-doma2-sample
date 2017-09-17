package com.sample.common.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageUtils {

    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * メッセージを取得します。
     * 
     * @param key
     * @param args
     * @return
     */
    public static String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return MessageUtils.messageSource.getMessage(key, args, locale);
    }

    /**
     * ロケールを指定してメッセージを取得します。
     *
     * @param key
     * @param locale
     * @param args
     * @return
     */
    public static String getMessage(String key, Locale locale, Object... args) {
        return MessageUtils.messageSource.getMessage(key, args, locale);
    }
}
