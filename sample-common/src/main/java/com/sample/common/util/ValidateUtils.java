package com.sample.common.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/** 入力チェックユーティリティ */
public class ValidateUtils {

  /**
   * 引数の値の真偽を示す値を返します。
   *
   * @param value
   * @return
   */
  public static boolean isTrue(Boolean value) {
    return value != null && value;
  }

  /**
   * 値の真偽を示す値を返します。
   *
   * @param value
   * @return
   */
  public static boolean isFalse(Boolean value) {
    return !isTrue(value);
  }

  /**
   * 値の存在可否をチェックします。
   *
   * @param value
   * @return
   */
  public static boolean isEmpty(String value) {
    return value == null || value.length() == 0;
  }

  /**
   * コレクションの存在可否をチェックします。
   *
   * @param collection
   * @return
   */
  public static boolean isEmpty(Collection<?> collection) {
    return (collection == null || collection.isEmpty());
  }

  /**
   * 配列の存在可否をチェックします。
   *
   * @param array
   * @return
   */
  public static boolean isEmpty(Object[] array) {
    return (array == null || array.length == 0);
  }

  /**
   * マップの存在可否をチェックします。
   *
   * @param map
   * @return
   */
  public static boolean isEmpty(Map<?, ?> map) {
    return (map == null || map.isEmpty());
  }

  /**
   * 存在可否をチェックします。
   *
   * @param value
   * @return
   */
  public static boolean isNotEmpty(String value) {
    return !isEmpty(value);
  }

  /**
   * 存在可否をチェックします。
   *
   * @param collection
   * @return
   */
  public static boolean isNotEmpty(Collection<?> collection) {
    return !isEmpty(collection);
  }

  /**
   * 存在可否をチェックします。
   *
   * @param array
   * @return
   */
  public static boolean isNotEmpty(Object[] array) {
    return !isEmpty(array);
  }

  /**
   * 存在可否をチェックします。
   *
   * @param map
   * @return
   */
  public static boolean isNotEmpty(Map<?, ?> map) {
    return !isEmpty(map);
  }

  /**
   * 文字列と正規表現のマッチ可否をチェックします。
   *
   * @param value
   * @param regex
   * @return
   */
  public static boolean matches(String value, String regex) {
    return isNotEmpty(value) && value.matches(regex);
  }

  /**
   * 値が数字のみかチェックします。
   *
   * @param value
   * @return
   */
  public static boolean isNumeric(String value) {
    return StringUtils.isNumeric(value);
  }

  /**
   * 値がASCII文字のみかチェックします。
   *
   * @param value
   * @return
   */
  public static boolean isAscii(String value) {
    return StringUtils.isAsciiPrintable(value);
  }

  /**
   * 引数同士が等しいかチェックします。
   *
   * @param obj1
   * @param obj2
   * @return
   */
  public static boolean isEquals(Object obj1, Object obj2) {
    return Objects.equals(obj1, obj2);
  }

  /**
   * 引数同士が等しいかチェックします。
   *
   * @param obj1
   * @param obj2
   * @return
   */
  public static boolean isNotEquals(Object obj1, Object obj2) {
    return !isEquals(obj1, obj2);
  }
}
