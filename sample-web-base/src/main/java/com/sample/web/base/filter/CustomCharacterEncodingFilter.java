package com.sample.web.base.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.CharacterEncodingFilter;

import lombok.Setter;

/**
 * コールバックを呼び出して動作をカスタムする
 */
@Setter
public class CustomCharacterEncodingFilter extends CharacterEncodingFilter {

    private Callback callback;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        super.setEncoding("UTF-8");
        super.setForceEncoding(true);

        // コールバックを呼び出す
        callback.call(request, this);

        super.doFilterInternal(request, response, filterChain);
    }

    public interface Callback {

        /**
         * 任意の処理を実行します。
         * 
         * @param request
         * @param customCharacterEncodingFilter
         */
        void call(HttpServletRequest request, CustomCharacterEncodingFilter customCharacterEncodingFilter);
    }
}
