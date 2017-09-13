package com.sample.web.base.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * 文字列の入力値をLocalDate型に変換する
 */
public class LocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter;

    /**
     * コンストラクタ
     * 
     * @param dateFormat
     */
    public LocalDateConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDate convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        return LocalDate.parse(source, formatter);
    }
}
