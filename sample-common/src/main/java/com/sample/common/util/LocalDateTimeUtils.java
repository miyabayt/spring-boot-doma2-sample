package com.sample.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 日付ユーティリティ
 */
@Slf4j
public class LocalDateTimeUtils {

    /**
     * Date型の値を指定されたDateTimeFormatterフォーマットした値を返します。
     *
     * @param fromDate
     * @param formatter
     * @return
     */
    public static String format(final Date fromDate, final DateTimeFormatter formatter) {
        val localDateTime = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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
    public static String format(final LocalDateTime fromLocalDateTime, final DateTimeFormatter formatter) {
        val result = formatter.format(fromLocalDateTime);
        return result;
    }
}
