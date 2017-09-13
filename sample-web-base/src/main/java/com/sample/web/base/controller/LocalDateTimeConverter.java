package com.sample.web.base.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * 文字列の入力値をLocalDateTime型に変換する
 */
public final class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private final DateTimeFormatter formatter;

    /**
     * コンストラクタ
     * 
     * @param dateFormat
     */
    public LocalDateTimeConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDateTime convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        return LocalDateTime.parse(source, formatter);
    }
}
