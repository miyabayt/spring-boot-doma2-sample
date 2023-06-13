package com.bigtreetc.sample.web.api.base;

import com.bigtreetc.sample.common.FunctionNameAware;
import com.bigtreetc.sample.web.api.base.resource.ResourceFactory;
import com.bigtreetc.sample.web.base.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** 基底APIコントローラー */
@ResponseStatus(HttpStatus.OK)
@Slf4j
public abstract class AbstractRestController extends BaseController implements FunctionNameAware {

  @Autowired protected ResourceFactory resourceFactory;
}
