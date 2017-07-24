package com.sample.web.base.controller.html;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.sample.domain.dto.common.ID;
import com.sample.web.base.controller.IDTypeEditor;

import lombok.extern.slf4j.Slf4j;

/**
 * HTMLコントローラーアドバイス
 */
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestControllerでは動作させない
@Slf4j
public class HtmlControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        // 文字列フィールドが未入力の場合に、空文字ではなくNULLをバインドする
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        // 文字列をIDに変換する
        binder.registerCustomEditor(ID.class, new IDTypeEditor());

        // idカラムを入力値で上書きしない
        binder.setDisallowedFields("*.id");

        // versionカラムを入力値で上書きしない
        binder.setDisallowedFields("*.version");
    }
}
