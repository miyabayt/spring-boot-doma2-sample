package com.sample.web.base.controller.api.resource;

import io.swagger.annotations.ApiModelProperty;

public interface PageableResource extends Resource {

    @ApiModelProperty(value = "ページ数")
    int getPage();

    @ApiModelProperty(value = "総ページ数")
    int getTotalPages();

    void setPage(int page);

    void setTotalPages(int totalPages);
}
