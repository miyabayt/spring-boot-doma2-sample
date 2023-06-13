package com.bigtreetc.sample.web.base.aop;

import com.bigtreetc.sample.common.util.MessageUtils;
import com.bigtreetc.sample.domain.entity.CodeCategory;
import com.bigtreetc.sample.domain.repository.CodeCategoryRepository;
import com.bigtreetc.sample.web.base.WebConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class SetModelAndViewInterceptor extends BaseHandlerInterceptor {

  @Autowired CodeCategoryRepository codeCategoryRepository;

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    // コントローラーの動作後
    if (modelAndView == null) {
      return;
    }

    val locale = LocaleContextHolder.getLocale();
    val pulldownOption = MessageUtils.getMessage(WebConst.MAV_PULLDOWN_OPTION, locale);

    // 定数定義を画面に渡す
    Map<String, Object> constants = new HashMap<>();
    constants.put(WebConst.MAV_PULLDOWN_OPTION, pulldownOption);
    modelAndView.addObject(WebConst.MAV_CONST, constants);

    // 定形のリスト等
    val codeCategories = getCodeCategories();
    modelAndView.addObject(WebConst.MAV_CODE_CATEGORIES, codeCategories);

    // 入力エラーを画面オブジェクトに設定する
    retainValidateErrors(modelAndView);
  }

  /**
   * コード分類マスタ一覧を画面に設定する
   *
   * @return
   */
  protected List<CodeCategory> getCodeCategories() {
    return codeCategoryRepository.fetchAll();
  }

  /**
   * 入力エラーを画面オブジェクトに設定する
   *
   * @param modelAndView
   */
  protected void retainValidateErrors(ModelAndView modelAndView) {
    val model = modelAndView.getModelMap();

    if (model.containsAttribute(WebConst.MAV_ERRORS)) {
      val errors = model.get(WebConst.MAV_ERRORS);

      if (errors instanceof BeanPropertyBindingResult) {
        val br = ((BeanPropertyBindingResult) errors);

        if (br.hasErrors()) {
          val formName = br.getObjectName();
          val key = BindingResult.MODEL_KEY_PREFIX + formName;
          model.addAttribute(key, errors);
          model.addAttribute(
              WebConst.ERROR_MESSAGE, MessageUtils.getMessage(WebConst.VALIDATION_ERROR));
        }
      }
    }
  }
}
