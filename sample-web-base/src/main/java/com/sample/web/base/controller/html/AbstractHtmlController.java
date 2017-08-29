package com.sample.web.base.controller.html;

import com.sample.common.FunctionNameAware;
import com.sample.web.base.controller.BaseController;
import com.sample.web.base.security.authorization.Authorizable;

import lombok.extern.slf4j.Slf4j;

/**
 * 基底HTMLコントローラー
 */
@Slf4j
public abstract class AbstractHtmlController extends BaseController
        implements FunctionNameAware, Authorizable {

    @Override
    public boolean authorityRequired() {
        return true;
    }
}
