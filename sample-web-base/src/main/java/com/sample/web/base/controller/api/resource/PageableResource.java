package com.sample.web.base.controller.api.resource;

public interface PageableResource extends Resource {

    int getPage();

    int getTotalPages();

    void setPage(int page);

    void setTotalPages(int totalPages);
}
