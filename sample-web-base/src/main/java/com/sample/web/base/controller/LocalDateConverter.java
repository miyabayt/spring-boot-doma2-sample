package com.sample.web.base.controller;

import static com.sample.common.util.ValidateUtils.isEmpty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;

/** 文字列の入力値をLocalDate型に変換する */
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
    if (isEmpty(source)) {
      return null;
    }

    return LocalDate.parse(source, formatter);
  }
}
