package com.sample.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/** 日付ユーティリティ */
@Slf4j
public class DateUtils {

  /**
   * Date型の値を指定されたDateTimeFormatterフォーマットした値を返します。
   *
   * @param fromDate
   * @param formatter
   * @return
   */
  public static String format(final Date fromDate, final DateTimeFormatter formatter) {
    val zoneId = getZoneId();
    val localDateTime = fromDate.toInstant().atZone(zoneId).toLocalDateTime();
    val result = formatter.format(localDateTime);
    return result;
  }

  /**
   * LocalDateTime型の値を指定されたDateTimeFormatterフォーマットした値を返します。
   *
   * @param fromLocalDateTime
   * @param formatter
   * @return
   */
  public static String format(
      final LocalDateTime fromLocalDateTime, final DateTimeFormatter formatter) {
    val result = formatter.format(fromLocalDateTime);
    return result;
  }

  /**
   * Date型の値をLocalDateTime型に変換して返します。
   *
   * @param fromDate
   * @return
   */
  public static LocalDateTime toLocalDateTime(final Date fromDate) {
    val zoneId = getZoneId();
    return fromDate.toInstant().atZone(zoneId).toLocalDateTime();
  }

  /**
   * LocalDateTime型の値をDate型に変換して返します。
   *
   * @param fromLocalDateTime
   * @return
   */
  public static Date toDate(final LocalDateTime fromLocalDateTime) {
    val zoneId = getZoneId();
    val zoneDateTime = ZonedDateTime.of(fromLocalDateTime, zoneId);
    return Date.from(zoneDateTime.toInstant());
  }

  /**
   * LocalDate型の値をDate型に変換して返します。
   *
   * @param localDate
   * @return
   */
  public static Date toDate(final LocalDate localDate) {
    val zoneId = getZoneId();
    val zoneDateTime = localDate.atStartOfDay(zoneId).toInstant();
    return Date.from(zoneDateTime);
  }

  protected static ZoneId getZoneId() {
    return ZoneId.systemDefault();
  }
}
