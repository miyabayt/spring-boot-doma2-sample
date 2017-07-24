package com.sample.web.base.controller.html;

import org.springframework.beans.factory.annotation.Autowired;

import com.sample.common.FunctionNameAware;
import com.sample.web.base.controller.BaseController;
import com.sample.web.base.service.CsvDownloadService;

import lombok.extern.slf4j.Slf4j;

/**
 * 基底HTMLコントローラー
 */
@Slf4j
public abstract class AbstractHtmlController extends BaseController implements FunctionNameAware {

    @Autowired
    protected CsvDownloadService csvDownloadService;
}
