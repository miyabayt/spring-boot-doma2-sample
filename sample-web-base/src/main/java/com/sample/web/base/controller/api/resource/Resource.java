package com.sample.web.base.controller.api.resource;

import java.util.List;

import com.sample.domain.dto.common.Dto;
import io.swagger.annotations.ApiModelProperty;

public interface Resource {

    @ApiModelProperty(value = "データ")
    List<? extends Dto> getData();

    void setData(List<? extends Dto> data);

    @ApiModelProperty(value = "メッセージ")
    String getMessage();

    void setMessage(String message);
}
