package com.bigtreetc.sample.web.base.controller.html;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/** HTMLコントローラーアドバイス */
@ControllerAdvice(assignableTypes = {AbstractHtmlController.class}) // RestControllerでは動作させない
@Slf4j
public class HtmlControllerAdvice {

  @InitBinder
  public void initBinder(WebDataBinder binder, HttpServletRequest request) {
    // 文字列フィールドが未入力の場合に、空文字ではなくNULLをバインドする
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

    // idカラムを入力値で上書きしない
    binder.setDisallowedFields("*.id");

    // versionカラムを入力値で上書きしない
    binder.setDisallowedFields("*.version");
  }
}
