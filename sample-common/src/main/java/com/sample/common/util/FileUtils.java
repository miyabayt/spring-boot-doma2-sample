package com.sample.common.util;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileUtils {

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
      throw new IllegalArgumentException("could not create directory. " + location.toString(), e);
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
