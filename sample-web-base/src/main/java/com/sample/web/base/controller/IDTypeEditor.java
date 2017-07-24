package com.sample.web.base.controller;

import java.beans.PropertyEditorSupport;

import com.sample.domain.dto.common.ID;

/**
 * 文字列をIDクラスに変換する
 */
public class IDTypeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            Integer id = Integer.valueOf(text);
            setValue(ID.of(id));
        } catch (NumberFormatException e) {
            // nop
        }
    }
}
