package com.sample.domain.dto.common;

/** ページング可能であることを示す */
public interface Pageable {

  Pageable DEFAULT = new DefaultPageable(1, 10);

  Pageable NO_LIMIT = new DefaultPageable(1, Integer.MAX_VALUE);

  /** @return */
  int getPage();

  /** @return */
  int getPerpage();
}
