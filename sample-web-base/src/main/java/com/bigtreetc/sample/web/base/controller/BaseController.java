package com.bigtreetc.sample.web.base.controller;

import com.bigtreetc.sample.common.util.MessageUtils;
import java.util.Locale;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class BaseController {

  public static final String VALIDATION_ERROR = "ValidationError";

  @Autowired protected ApplicationContext applicationContext;

  @Autowired protected ModelMapper modelMapper;

  /**
   * 入力エラーの共通メッセージを返します。
   *
   * @return
   */
  protected String getValidationErrorMessage() {
    return getMessage(VALIDATION_ERROR);
  }

  /**
   * コンテキストを返します。
   *
   * @return
   */
  protected ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * メッセージを取得します。
   *
   * @param key
   * @param args
   * @return
   */
  protected String getMessage(String key, Object... args) {
    return MessageUtils.getMessage(key, args);
  }

  /**
   * ロケールを指定してメッセージを取得します。
   *
   * @param key
   * @param args
   * @param locale
   * @return
   */
  protected String getMessage(String key, Object[] args, Locale locale) {
    return MessageUtils.getMessage(key, args, locale);
  }
}
