package com.sample.web.base.aop;

import static com.sample.web.base.WebConst.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sample.domain.dto.CodeCategory;
import com.sample.domain.dto.common.DefaultPageable;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.service.codecategory.CodeCategoryService;
import com.sample.web.base.util.MessageUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetModelAndViewInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    CodeCategoryService codeCategoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // コントローラーの動作後
        val locale = LocaleContextHolder.getLocale();
        val pulldownOption = MessageUtils.getMessage(MAV_PULLDOWN_OPTION, locale);

        // 定数定義
        Map<String, Object> constants = new HashMap<>();
        constants.put(MAV_PULLDOWN_OPTION, pulldownOption);

        // 定形のリスト等
        val codeCategories = getCodeCategories();

        // 画面に値を渡す
        modelAndView.addObject(MAV_CONST, constants);
        modelAndView.addObject(MAV_CODE_CATEGORIES, codeCategories);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 処理完了後
    }

    protected List<CodeCategory> getCodeCategories() {
        List<CodeCategory> data = Collections.emptyList();

        // 検索条件を作成する
        val where = new CodeCategory();

        // コード分類をすべて取得する
        val codeCategories = codeCategoryService.findAll(where, Pageable.NO_LIMIT_PAGEABLE);

        if (codeCategories != null) {
            data = codeCategories.getData();
        }

        return data;
    }
}
