package com.bigtreetc.sample.web.api.base.resource;

public interface PageableResource extends Resource {

  int getPage();

  int getTotalPages();

  void setPage(int page);

  void setTotalPages(int totalPages);
}
