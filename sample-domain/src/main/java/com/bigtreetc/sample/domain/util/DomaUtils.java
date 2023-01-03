package com.bigtreetc.sample.domain.util;

import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.data.domain.Pageable;

/** Doma関連ユーティリティ */
public class DomaUtils {

  /**
   * SearchOptionsを作成して返します。
   *
   * @return
   */
  public static SelectOptions createSelectOptions() {
    return SelectOptions.get();
  }

  /**
   * SearchOptionsを作成して返します。
   *
   * @param pageable
   * @return
   */
  public static SelectOptions createSelectOptions(Pageable pageable) {
    if (pageable.isUnpaged()) {
      return SelectOptions.get();
    }
    int page = pageable.getPageNumber();
    int perpage = pageable.getPageSize();
    return createSelectOptions(page, perpage);
  }

  /**
   * SearchOptionsを作成して返します。
   *
   * @param page
   * @param perpage
   * @return
   */
  public static SelectOptions createSelectOptions(int page, int perpage) {
    int offset = page * perpage;
    return SelectOptions.get().offset(offset).limit(perpage);
  }
}
