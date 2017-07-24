package com.sample.web.base.controller.html;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseSearchForm extends BaseForm {

    private static final long serialVersionUID = -7129975017789825804L;

    int page = 1;

    int perpage = 10;
}
