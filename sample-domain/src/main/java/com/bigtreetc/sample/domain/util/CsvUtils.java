package com.bigtreetc.sample.domain.util;

import static com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/** TSVファイル出力ユーティリティ */
@Slf4j
public class CsvUtils {

  private static final CsvMapper csvMapper = createCsvMapper();

  /**
   * CSVマッパーを生成する。
   *
   * @return
   */
  private static CsvMapper createCsvMapper() {
    val mapper = new CsvMapper();
    mapper.configure(ALWAYS_QUOTE_STRINGS, true);
    mapper.findAndRegisterModules();
    return mapper;
  }

  /**
   * CSVファイルを出力します。
   *
   * @param clazz
   * @param data
   */
  public static void writeCsv(
      OutputStream outputStream, Class<?> clazz, Stream<?> data, Function<Object, ?> mapper)
      throws IOException {
    write(outputStream, clazz, data, mapper, StandardCharsets.UTF_8.name(), ',');
  }

  /**
   * CSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @param charsetName
   */
  public static void writeCsv(
      OutputStream outputStream,
      Class<?> clazz,
      Stream<?> data,
      Function<Object, ?> mapper,
      String charsetName)
      throws IOException {
    write(outputStream, clazz, data, mapper, charsetName, ',');
  }

  /**
   * TSVファイルを出力します。
   *
   * @param clazz
   * @param data
   */
  public static void writeTsv(
      OutputStream outputStream, Class<?> clazz, Stream<?> data, Function<Object, ?> mapper)
      throws IOException {
    write(outputStream, clazz, data, mapper, StandardCharsets.UTF_8.name(), '\t');
  }

  /**
   * TSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @param charsetName
   */
  public static void writeTsv(
      OutputStream outputStream,
      Class<?> clazz,
      Stream<?> data,
      Function<Object, ?> mapper,
      String charsetName)
      throws IOException {
    write(outputStream, clazz, data, mapper, charsetName, '\t');
  }

  /**
   * CSVファイルを出力します。
   *
   * @param outputStream
   * @param clazz
   * @param data
   * @param charsetName
   * @param delimiter
   * @return
   */
  private static void write(
      OutputStream outputStream,
      Class<?> clazz,
      Stream<?> data,
      Function<Object, ?> mapper,
      String charsetName,
      char delimiter)
      throws IOException {
    // CSVスキーマ、デリミタをセットする
    val schema = csvMapper.schemaFor(clazz).withColumnSeparator(delimiter);

    // 書き出し
    try (val writer = new BufferedWriter(new OutputStreamWriter(outputStream, charsetName))) {
      boolean header = true;
      val iterator = data.iterator();
      while (iterator.hasNext()) {
        val obj = iterator.next();
        val mapped = (mapper != null) ? mapper.apply(obj) : obj;
        val csvWriter =
            (header)
                ? csvMapper.writer(schema.withHeader())
                : csvMapper.writer(schema.withoutHeader());
        val csvLine = csvWriter.writeValueAsString(mapped);
        writer.write(csvLine);
        header = false;
      }
    }
  }
}
