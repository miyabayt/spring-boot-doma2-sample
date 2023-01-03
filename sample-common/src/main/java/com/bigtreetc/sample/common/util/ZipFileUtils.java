package com.bigtreetc.sample.common.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class ZipFileUtils {

  private static Charset CHARSET_MS932 = Charset.forName("MS932");

  /**
   * Zipファイルを展開しながら1ファイルずつ変換します。
   *
   * @param inputStream
   * @param consumer
   * @return
   */
  public static <T> List<T> transform(
      final InputStream inputStream, BiFunction<ZipInputStream, ZipEntry, T> consumer) {
    List<T> list = null;
    File tempFile = null;

    try {
      tempFile = toTempFile(inputStream);
      list = transform(toFileInputStream(tempFile), StandardCharsets.UTF_8, consumer);
    } catch (IllegalArgumentException e) {
      if (tempFile != null) {
        list = transform(toFileInputStream(tempFile), CHARSET_MS932, consumer);
      }
    } finally {
      try {
        if (tempFile != null) {
          tempFile.deleteOnExit();
        }
        inputStream.close();
      } catch (IOException e) {
        // ignore
      }
    }

    return list;
  }

  private static <T> List<T> transform(
      final InputStream inputStream,
      Charset charset,
      BiFunction<ZipInputStream, ZipEntry, T> consumer) {
    val list = new ArrayList<T>();
    try (val zis = new ZipInputStream(inputStream, charset)) {
      ZipEntry zipEntry;
      while ((zipEntry = zis.getNextEntry()) != null) {
        if (log.isDebugEnabled()) {
          log.debug("read file. [filename={}]", zipEntry.getName());
        }
        list.add(consumer.apply(zis, zipEntry));
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return list;
  }

  @SneakyThrows
  private static FileInputStream toFileInputStream(File file) {
    return new FileInputStream(file);
  }

  @SneakyThrows
  private static File toTempFile(InputStream inputStream) {
    val tempFile = FileUtils.createTempFile(ZipFileUtils.class.getSimpleName());
    try (val fileOutputStream = new FileOutputStream(tempFile)) {
      inputStream.transferTo(fileOutputStream);
      return tempFile;
    }
  }
}
