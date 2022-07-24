package com.sample.domain.dao;

import java.time.LocalDateTime;

/** 監査情報ホルダー */
public class AuditInfoHolder {

  private static final ThreadLocal<String> AUDIT_USER = new ThreadLocal<>();

  private static final ThreadLocal<LocalDateTime> AUDIT_DATE_TIME = new ThreadLocal<>();

  /**
   * 監査情報を保存します。
   *
   * @param username
   */
  public static void set(String username, LocalDateTime localDateTime) {
    AUDIT_USER.set(username);
    AUDIT_DATE_TIME.set(localDateTime);
  }

  /**
   * 監査ユーザーを返します。
   *
   * @return
   */
  public static String getAuditUser() {
    return AUDIT_USER.get();
  }

  /**
   * 監査時刻を返します。
   *
   * @return
   */
  public static LocalDateTime getAuditDateTime() {
    return AUDIT_DATE_TIME.get();
  }

  /** 監査情報をクリアします。 */
  public static void clear() {
    AUDIT_USER.remove();
    AUDIT_DATE_TIME.remove();
  }
}
