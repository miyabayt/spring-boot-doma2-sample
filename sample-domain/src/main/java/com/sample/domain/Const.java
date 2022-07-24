package com.sample.domain;

import java.time.format.DateTimeFormatter;

/** 定数定義 */
public class Const {

  // yyyy/MM/dd HH:mm:ss
  public static final DateTimeFormatter YYYY_MM_DD_HHmmss =
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
}
