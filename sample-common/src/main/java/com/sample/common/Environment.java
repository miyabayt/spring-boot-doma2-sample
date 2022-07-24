package com.sample.common;

import java.io.File;

/** 環境情報 */
public enum Environment {
  LOCAL,
  DEV,
  STG,
  PRODUCTION,
  UNKNOWN;

  private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

  private static final String LOCAL_PROFILE = "local";

  private static final String DEV_PROFILE = "development";

  private static final String STG_PROFILE = "staging";

  private static final String PRODUCTION_PROFILE = "production";

  /**
   * 環境を判別して返します。
   *
   * @return
   */
  public static Environment get() {
    // システムプロパティに設定したspringプロファイルを取得する
    String environment = System.getProperty(SPRING_PROFILES_ACTIVE);

    if (LOCAL_PROFILE.equalsIgnoreCase(environment)) {
      return LOCAL;
    } else if (DEV_PROFILE.equalsIgnoreCase(environment)) {
      return DEV;
    } else if (STG_PROFILE.equalsIgnoreCase(environment)) {
      return STG;
    } else if (PRODUCTION_PROFILE.equalsIgnoreCase(environment)) {
      return PRODUCTION;
    }

    return UNKNOWN;
  }

  /**
   * OSがWindowsの場合はtrueを返します。
   *
   * @return
   */
  public static boolean isWindowsOs() {
    return File.separator.equals("\\");
  }
}
