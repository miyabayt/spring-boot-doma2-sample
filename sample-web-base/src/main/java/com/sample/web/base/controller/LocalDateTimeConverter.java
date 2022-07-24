package com.sample.web.base.controller;

import static com.sample.common.util.ValidateUtils.isEmpty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;

/** 文字列の入力値をLocalDateTime型に変換する */
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
    if (isEmpty(source)) {
      return null;
    }

    return LocalDateTime.parse(source, formatter);
  }
}
