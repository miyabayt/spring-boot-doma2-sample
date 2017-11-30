package com.sample.web.base.aop;

import static com.sample.web.base.WebConst.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.sample.common.util.MessageUtils;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.service.system.CodeCategoryService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetModelAndViewInterceptor extends BaseHandlerInterceptor {

    @Autowired
    CodeCategoryService codeCategoryService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // コントローラーの動作後
        if (isRestController(handler)) {
            // APIの場合はスキップする
            return;
        }

        if (modelAndView == null) {
            return;
        }

        val locale = LocaleContextHolder.getLocale();
        val pulldownOption = MessageUtils.getMessage(MAV_PULLDOWN_OPTION, locale);

        // 定数定義を画面に渡す
        Map<String, Object> constants = new HashMap<>();
        constants.put(MAV_PULLDOWN_OPTION, pulldownOption);
        modelAndView.addObject(MAV_CONST, constants);

        // 定形のリスト等
        val codeCategories = getCodeCategories();
        modelAndView.addObject(MAV_CODE_CATEGORIES, codeCategories);

        // 入力エラーを画面オブジェクトに設定する
        retainValidateErrors(modelAndView);
    }

    /**
     * コード分類一覧を画面に設定する
     * 
     * @return
     */
    protected Page<CodeCategory> getCodeCategories() {
        // コード分類をすべて取得する
        return codeCategoryService.fetchAll();
    }

    /**
     * 入力エラーを画面オブジェクトに設定する
     * 
     * @param modelAndView
     */
    protected void retainValidateErrors(ModelAndView modelAndView) {
        val model = modelAndView.getModelMap();

        if (model != null && model.containsAttribute(MAV_ERRORS)) {
            val errors = model.get(MAV_ERRORS);

            if (errors != null && errors instanceof BeanPropertyBindingResult) {
                val br = ((BeanPropertyBindingResult) errors);

                if (br.hasErrors()) {
                    val formName = br.getObjectName();
                    val key = BindingResult.MODEL_KEY_PREFIX + formName;
                    model.addAttribute(key, errors);
                    model.addAttribute(GLOBAL_MESSAGE, MessageUtils.getMessage(VALIDATION_ERROR));
                }
            }
        }
    }
}
