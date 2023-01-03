package com.bigtreetc.sample.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class FileUtils {

  /**
   * 一時ファイルを作成します。
   *
   * @param suffix
   * @return
   */
  public static File createTempFile(String suffix) {
    val tmpDir = System.getProperty("java.io.tmpdir");
    try {
      return File.createTempFile(tmpDir, suffix);
    } catch (IOException e) {
      throw new IllegalArgumentException("could not create temp file. " + suffix, e);
    }
  }

  /**
   * ディレクトリがない場合は作成します。
   *
   * @param location
   */
  public static void createDirectory(Path location) {
    try {
      Files.createDirectory(location);
    } catch (FileAlreadyExistsException ignore) {
      // ignore
    } catch (IOException e) {
      throw new IllegalArgumentException("could not create directory. " + location.toString(), e);
    }
  }

  /**
   * 親ディレクトリを含めてディレクトリがない場合は作成します。
   *
   * @param location
   */
  public static void createDirectories(Path location) {
    try {
      Files.createDirectories(location);
    } catch (FileAlreadyExistsException ignore) {
      // ignore
    } catch (IOException e) {
      throw new IllegalArgumentException("could not create directory. " + location, e);
    }
  }

  /**
   * ファイルの一覧を取得します。
   *
   * @param location
   * @return
   */
  public static List<Path> listAllFiles(Path location) {
    try (val walk = Files.walk(location, 1)) {
      return walk.filter(path -> !path.equals(location))
          .map(location::relativize)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new IllegalArgumentException("failed to list uploaded files. ", e);
    }
  }

  /**
   * ファイルを読み込みます。
   *
   * @param location
   * @param filename
   * @return
   */
  @SneakyThrows
  public static Resource loadFile(Path location, String filename) {
    try {
      val file = location.resolve(filename);
      val resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      }

      throw new FileNotFoundException("could not read file. " + filename);

    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(
          "malformed Url resource. [location=" + location + ", filename=" + filename + "]", e);
    }
  }

  /**
   * ファイルを削除します。
   *
   * @param location
   * @param filename
   */
  public static void deleteFile(Path location, String filename) {
    Objects.requireNonNull(filename, "filename can't be null");
    try {
      Files.deleteIfExists(location.resolve(filename));
    } catch (IOException e) {
      throw new IllegalStateException("failed to delete file. " + filename, e);
    }
  }
}
